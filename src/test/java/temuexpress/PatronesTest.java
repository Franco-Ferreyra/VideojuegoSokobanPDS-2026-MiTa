package temuexpress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import temuexpress.controlador.ControladorJuego;
import temuexpress.modelo.creacion.ConstructorNivelSokoban;
import temuexpress.modelo.creacion.LectorNivel;
import temuexpress.modelo.dto.ResumenNivelDTO;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.modelo.nucleo.Partida;
import temuexpress.modelo.puntaje.BonusPorPar;
import temuexpress.modelo.puntaje.BonusSinUndo;
import temuexpress.modelo.puntaje.CalculadorPuntaje;
import temuexpress.modelo.puntaje.PuntajeBase;
import temuexpress.modelo.puntaje.RecargoUndo;

/** Decorator de puntaje y Observer de la partida. */
class PatronesTest {
    private final CalculadorPuntaje normal = new BonusPorPar(new BonusSinUndo(new PuntajeBase()));

    @Test
    void puntajeSumaLosBonusSinUndoYPorPar() {
        // 1000 - 3*28 - 5*8 = 876; +200 sin Undo; +150 por PAR.
        assertEquals(1226, normal.calcular(new ResumenNivelDTO(28, 8, 0, 34)));
        // Pasado del PAR y con un Undo: 1000 - 105 - 40 - 100 = 755, sin bonus.
        assertEquals(755, normal.calcular(new ResumenNivelDTO(35, 8, 1, 34)));
    }

    @Test
    void recargoDeUndoDescuentaCienMasPorUso() {
        CalculadorPuntaje argChile = new RecargoUndo(normal);
        // 1000 - 174 - 110 - 200 = 516; +150 por PAR; -200 de recargo.
        assertEquals(466, argChile.calcular(new ResumenNivelDTO(58, 22, 2, 70)));
    }

    @Test
    void partidaAvisaCadaAccionASusObservadores() throws Exception {
        Nivel nivel =
                new LectorNivel(new ConstructorNivelSokoban())
                        .construir(Path.of("niveles", "nivel_1-1.txt"), "1-1");
        Partida partida = new Partida(nivel);
        List<String> tipos = new ArrayList<>();
        partida.suscribir(e -> tipos.add(e.getTipo()));
        assertTrue(partida.mover(Direccion.IZQUIERDA));
        assertEquals(List.of("MOVIMIENTO"), tipos);
        partida.reiniciar();
        assertEquals("REINICIO", tipos.get(tipos.size() - 1));
    }

    @Test
    void elUltimoEventoDeLaSolucionEsVictoria() throws Exception {
        Nivel nivel =
                new LectorNivel(new ConstructorNivelSokoban())
                        .construir(Path.of("niveles", "nivel_2-1.txt"), "2-1");
        ControladorJuego juego = new ControladorJuego(nivel);
        List<String> tipos = new ArrayList<>();
        // El controlador sin ventana no suscribe a nadie: se observa la partida por un mover.
        Partida espia = new Partida(nivel);
        espia.suscribir(e -> tipos.add(e.getTipo()));
        for (char letra : "AAAAIBBADDDDDDDIBIBDD".toCharArray()) {
            Direccion dir =
                    switch (letra) {
                        case 'A' -> Direccion.ARRIBA;
                        case 'B' -> Direccion.ABAJO;
                        case 'I' -> Direccion.IZQUIERDA;
                        default -> Direccion.DERECHA;
                    };
            assertTrue(juego.mover(dir));
            assertTrue(espia.mover(dir));
        }
        assertTrue(tipos.contains("MURO_ABRE"));
        assertTrue(tipos.contains("FRAGIL_DANO"));
        assertTrue(tipos.contains("META"));
        assertEquals("VICTORIA", tipos.get(tipos.size() - 1));
    }
}
