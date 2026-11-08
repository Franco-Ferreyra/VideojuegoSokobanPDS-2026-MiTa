package temuexpress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static temuexpress.modelo.nucleo.Direccion.ABAJO;
import static temuexpress.modelo.nucleo.Direccion.ARRIBA;
import static temuexpress.modelo.nucleo.Direccion.DERECHA;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import temuexpress.controlador.ControladorJuego;
import temuexpress.modelo.creacion.ConstructorNivelSokoban;
import temuexpress.modelo.creacion.LectorNivel;
import temuexpress.modelo.nucleo.MementoPartida;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.modelo.nucleo.Partida;
import temuexpress.modelo.nucleo.Posicion;

class MotorBasicoTest {
    @TempDir Path temporal;
    private final LectorNivel lector = new LectorNivel(new ConstructorNivelSokoban());

    private String documento(String[] terreno, String[] objetos) {
        return "REGION=CHINA\nVARIANTE=NINGUNA\nPAR=4\n[TERRENO]\n"
                + String.join("\n", terreno)
                + "\n[OBJETOS]\n"
                + String.join("\n", objetos)
                + "\n";
    }

    private String tutorial() {
        return documento(
                new String[] {"#######", "#    .#", "#     #", "#######"},
                new String[] {"       ", "  @$   ", "       ", "       "});
    }

    private Nivel cargar(String texto) throws IOException {
        Path archivo = temporal.resolve("nivel.txt");
        Files.writeString(archivo, texto);
        return lector.construir(archivo, "1-1");
    }

    private ControladorJuego juego() throws IOException {
        return new ControladorJuego(cargar(tutorial()));
    }

    private void caminar(ControladorJuego juego, int pasos) {
        for (int i = 0; i < pasos; i++) assertTrue(juego.mover(i % 2 == 0 ? ABAJO : ARRIBA));
    }

    @Test
    void victoriaExigeEntregarTodasLasCajasYUndoRestauraAmbas() throws Exception {
        Nivel nivel =
                cargar(
                        documento(
                                new String[] {"#######", "#  .  #", "#   . #", "#######"},
                                new String[] {"       ", "  $    ", " @ $   ", "       "}));
        ControladorJuego juego = new ControladorJuego(nivel);
        var inicial = juego.getTableroDTO();
        assertTrue(juego.mover(ARRIBA));
        assertTrue(juego.mover(DERECHA));
        assertFalse(juego.esVictoria());
        assertTrue(juego.mover(ABAJO));
        assertTrue(juego.mover(DERECHA));
        assertTrue(juego.esVictoria());
        assertEquals(2, juego.getEmpujes());
        assertTrue(juego.deshacer());
        assertEquals(inicial, juego.getTableroDTO());
        assertFalse(juego.esVictoria());
    }

    @Test
    void cargaDosCapasYMetadatosSinPerderEspacios() throws Exception {
        Nivel nivel = cargar("\uFEFF" + tutorial().replace("\n", "\r\n"));
        assertEquals("CHINA", nivel.getRegion());
        assertEquals("NINGUNA", nivel.getVariante());
        assertEquals("1-1", nivel.getNumero());
        assertEquals(4, nivel.getPar());
        assertEquals(7, nivel.getTablero().getAncho());
        assertEquals(4, nivel.getTablero().getAlto());
        assertEquals(new Posicion(1, 2), nivel.getTablero().getPosicionJugador());
    }

    @Test
    void tutorialDelRepositorioSeResuelveEnDosEmpujes() throws Exception {
        ControladorJuego juego =
                new ControladorJuego(
                        lector.construir(
                                Path.of("src/test/resources/niveles/tutorial.txt"), "tutorial"));
        assertFalse(juego.esVictoria());
        assertTrue(juego.mover(DERECHA));
        assertTrue(juego.mover(DERECHA));
        assertTrue(juego.esVictoria());
        assertEquals(2, juego.getMovimientos());
        assertEquals(2, juego.getEmpujes());
        assertFalse(juego.mover(ABAJO));
    }

    @Test
    void caminarNoEmpujaYBloqueoNoCuentaNiGuardaHistorial() throws Exception {
        ControladorJuego juego = juego();
        var inicial = juego.getTableroDTO();
        assertFalse(juego.mover(ARRIBA));
        assertEquals(inicial, juego.getTableroDTO());
        assertEquals(0, juego.getMovimientos());
        assertFalse(juego.deshacer());
        assertEquals(0, juego.getUsosUndo());
        assertTrue(juego.mover(ABAJO));
        assertEquals(1, juego.getMovimientos());
        assertEquals(0, juego.getEmpujes());
    }

    @Test
    void noEmpujaDosCajasNiCajaContraPared() throws Exception {
        String doble =
                documento(
                        new String[] {"#######", "#   ..#", "#     #", "#######"},
                        new String[] {"       ", "  @$$  ", "       ", "       "});
        ControladorJuego juego = new ControladorJuego(cargar(doble));
        var antes = juego.getTableroDTO();
        assertFalse(juego.mover(DERECHA));
        assertEquals(antes, juego.getTableroDTO());
        String pared =
                documento(
                        new String[] {"#######", "#   #.#", "#     #", "#######"},
                        new String[] {"       ", "  @$   ", "       ", "       "});
        juego = new ControladorJuego(cargar(pared));
        antes = juego.getTableroDTO();
        assertFalse(juego.mover(DERECHA));
        assertEquals(antes, juego.getTableroDTO());
        assertEquals(0, juego.getEmpujes());
    }

    @Test
    void undoParcialRestauraVictoriaYCajasPeroConservaContadores() throws Exception {
        ControladorJuego juego = juego();
        var inicial = juego.getTableroDTO();
        juego.mover(DERECHA);
        juego.mover(DERECHA);
        assertTrue(juego.esVictoria());
        assertTrue(juego.deshacer());
        assertEquals(inicial, juego.getTableroDTO());
        assertFalse(juego.esVictoria());
        assertEquals(2, juego.getMovimientos());
        assertEquals(2, juego.getEmpujes());
        assertEquals(1, juego.getUsosUndo());
        assertEquals(1, juego.getUsosUndoSeguidos());
        assertFalse(juego.deshacer());
        assertEquals(1, juego.getUsosUndo());
    }

    @Test
    void undoRetrocedeCincoYRetieneSoloLosUltimosQuince() throws Exception {
        ControladorJuego juego = juego();
        caminar(juego, 19);
        assertTrue(juego.deshacer()); // estado previo al movimiento 15: 14 movimientos
        assertEquals('@', juego.getTableroDTO().objetos().split("\n")[1].charAt(2));
        assertTrue(juego.deshacer()); // estado tras 9 movimientos
        assertEquals('@', juego.getTableroDTO().objetos().split("\n")[2].charAt(2));
        assertTrue(
                juego.deshacer()); // estado tras 4 movimientos (los primeros cuatro se descartaron)
        assertEquals('@', juego.getTableroDTO().objetos().split("\n")[1].charAt(2));
        assertFalse(juego.deshacer());
        assertEquals(3, juego.getUsosUndoSeguidos());
        assertEquals(19, juego.getMovimientos());
        assertFalse(juego.mover(ARRIBA));
        assertEquals(3, juego.getUsosUndoSeguidos());
        assertTrue(juego.mover(ABAJO));
        assertEquals(0, juego.getUsosUndoSeguidos());
        assertTrue(juego.deshacer());
        assertEquals(4, juego.getUsosUndo());
    }

    @Test
    void reinicioRestauraInicioContadoresEHistorial() throws Exception {
        ControladorJuego juego = juego();
        var inicial = juego.getTableroDTO();
        juego.mover(DERECHA);
        juego.deshacer();
        juego.mover(ABAJO);
        juego.reiniciarNivel();
        assertEquals(inicial, juego.getTableroDTO());
        assertEquals(0, juego.getMovimientos());
        assertEquals(0, juego.getEmpujes());
        assertEquals(0, juego.getUsosUndo());
        assertEquals(0, juego.getUsosUndoSeguidos());
        assertFalse(juego.deshacer());
    }

    @Test
    void cambiarNivelLimpiaHistorialYPartidasNoCompartenEstado() throws Exception {
        Nivel nivel = cargar(tutorial());
        ControladorJuego uno = new ControladorJuego(nivel), dos = new ControladorJuego(nivel);
        var inicial = dos.getTableroDTO();
        uno.mover(DERECHA);
        assertEquals(inicial, dos.getTableroDTO());
        uno.iniciarNivel(nivel);
        assertEquals(inicial, uno.getTableroDTO());
        assertFalse(uno.deshacer());
        assertEquals(0, uno.getMovimientos());
    }

    @Test
    void mementoEsIndependienteDeCambiosPosterioresYRechazaOtraPartida() throws Exception {
        Nivel nivel = cargar(tutorial());
        Partida partida = new Partida(nivel);
        var inicial = partida.getTableroDTO();
        MementoPartida m = partida.crearMemento();
        partida.mover(DERECHA);
        partida.restaurar(m);
        partida.mover(ABAJO);
        partida.restaurar(m);
        assertEquals(inicial, partida.getTableroDTO());
        assertThrows(IllegalArgumentException.class, () -> new Partida(nivel).restaurar(m));
    }

    @ParameterizedTest
    @ValueSource(strings = {"L", "W", "O", "~", ">", "<", "^", "v", "1", "9"})
    void mecanicasPendientesSeRechazanExplicitamente(String simbolo) {
        var e =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> cargar(tutorial().replace("#     #", "#  " + simbolo + "  #")));
        assertTrue(e.getMessage().contains("pendiente"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"F", "K"})
    void cajasPendientesSeRechazanExplicitamente(String simbolo) {
        assertTrue(
                assertThrows(
                                IllegalArgumentException.class,
                                () -> cargar(tutorial().replace("$", simbolo)))
                        .getMessage()
                        .contains("pendiente"));
    }

    @Test
    void rechazaArchivosInvalidosYPermiteReutilizarLector() throws Exception {
        List<String> invalidos =
                List.of(
                        tutorial().replace("REGION=CHINA", "REGION:CHINA"),
                        tutorial().replace("REGION=CHINA", "REGION=OTRA"),
                        tutorial().replace("VARIANTE=NINGUNA", "VARIANTE=ARG-CHILE"),
                        tutorial().replace("PAR=4", "PAR=-1"),
                        tutorial().replace("PAR=4", "PAR=x"),
                        tutorial().replace("PAR=4\n", ""),
                        tutorial().replace("PAR=4", "PAR=4\nPAR=5"),
                        tutorial().replace("[TERRENO]", "[OTRA]"),
                        tutorial().replace("[OBJETOS]", "[TERRENO]"),
                        tutorial().replace("[TERRENO]", "[TERRENO]\n[TERRENO]"),
                        tutorial().replace("#    .#", "#   .#"),
                        tutorial().replace("#    .#", "#     #"),
                        tutorial().replace("#######", "#     #"),
                        tutorial().replace("@", " "),
                        tutorial().replace("$", "@"),
                        tutorial().replace("  @$   ", "@  $   "),
                        tutorial().replace("$", "?"),
                        tutorial() + "       \n",
                        tutorial().replace("#######\n#    .#", "#\t#####\n#    .#"));
        for (String texto : invalidos)
            assertThrows(IllegalArgumentException.class, () -> cargar(texto), texto);
        assertNotNull(cargar(tutorial()));
    }

    @Test
    void validaTamanoMaximoYNoAsumeSolucion() throws Exception {
        String[] t = {
            "#############",
            "#          .#",
            "#           #",
            "#           #",
            "#           #",
            "#           #",
            "#############"
        };
        String[] o = {
            "             ",
            " @$          ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             "
        };
        assertNotNull(cargar(documento(t, o)));
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        cargar(
                                documento(
                                        Arrays.stream(t).map(s -> s + "#").toArray(String[]::new),
                                        Arrays.stream(o)
                                                .map(s -> s + " ")
                                                .toArray(String[]::new))));
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        cargar(
                                documento(
                                        new String[] {
                                            t[0], t[1], t[2], t[3], t[4], t[5], t[5], t[6]
                                        },
                                        new String[] {
                                            o[0], o[1], o[2], o[3], o[4], o[5], o[5], o[6]
                                        })));
    }
}
