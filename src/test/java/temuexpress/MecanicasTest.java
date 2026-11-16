package temuexpress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static temuexpress.modelo.nucleo.Direccion.ABAJO;
import static temuexpress.modelo.nucleo.Direccion.ARRIBA;
import static temuexpress.modelo.nucleo.Direccion.DERECHA;
import static temuexpress.modelo.nucleo.Direccion.IZQUIERDA;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import temuexpress.controlador.ControladorJuego;
import temuexpress.modelo.creacion.ConstructorNivelSokoban;
import temuexpress.modelo.creacion.LectorNivel;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.modelo.nucleo.Partida;

/** Mecánicas de China (llave, cerrojo y muro) y EEUU (caja frágil). */
class MecanicasTest {
    @TempDir Path temporal;
    private final LectorNivel lector = new LectorNivel(new ConstructorNivelSokoban());

    private Nivel cargar(String... filas) throws IOException {
        int mitad = filas.length / 2;
        String texto =
                "REGION=CHINA\nVARIANTE=NINGUNA\nPAR=10\n[TERRENO]\n"
                        + String.join("\n", java.util.Arrays.copyOfRange(filas, 0, mitad))
                        + "\n[OBJETOS]\n"
                        + String.join(
                                "\n", java.util.Arrays.copyOfRange(filas, mitad, filas.length))
                        + "\n";
        Path archivo = temporal.resolve("nivel.txt");
        Files.writeString(archivo, texto);
        return lector.construir(archivo, "1-1");
    }

    private static Direccion direccion(char letra) {
        return switch (letra) {
            case 'A' -> ARRIBA;
            case 'B' -> ABAJO;
            case 'I' -> IZQUIERDA;
            case 'D' -> DERECHA;
            default -> throw new IllegalArgumentException("Letra invalida: " + letra);
        };
    }

    private static char objetoEn(Partida partida, int fila, int columna) {
        return partida.getTableroDTO().objetos().split("\n")[fila].charAt(columna);
    }

    private static char terrenoEn(Partida partida, int fila, int columna) {
        return partida.getTableroDTO().terreno().split("\n")[fila].charAt(columna);
    }

    @ParameterizedTest
    @CsvSource({
        "1-1, IIADDAIIADDADDBBIDADDBBDDADB, 28, 8",
        "2-1, AAAAIBBADDDDDDDIBIBDD, 21, 8",
        "3-1, ADDDIIIBBDDDIIAIADDDDDDIIIIBIBDDDDDADD, 38, 21",
        "4-1, BBDDIIADDIAIADDBIBDADDBBDDAIADADDDADBIIIIBBDDDIIIAIIADDDDD, 58, 22",
        "4-2, ADABDAADAIIIBDDDIBDIBBDDAABDDADAIADDBIDBDDAAIBBBDBI, 51, 22",
        "4-3, ADBDBDBBIADDIIABIIAADDDIIBBAADDDDDDADBBBIBDDAAIAIIIIIIIADBBDDBBDAAIADDDADBBBIBD, 79, 29"
    })
    void solucionesDeSolucionesMdGananElNivel(String numero, String pasos, int movs, int emp)
            throws Exception {
        Nivel nivel = lector.construir(Path.of("niveles", "nivel_" + numero + ".txt"), numero);
        ControladorJuego juego = new ControladorJuego(nivel);
        for (int i = 0; i < pasos.length(); i++) {
            assertFalse(juego.esVictoria(), "Gano antes de tiempo en el paso " + i);
            assertTrue(juego.mover(direccion(pasos.charAt(i))), "Bloqueado en el paso " + i);
        }
        assertTrue(juego.esVictoria());
        assertEquals(movs, juego.getMovimientos());
        assertEquals(emp, juego.getEmpujes());
    }

    @Test
    void muroCerradoBloqueaYSeAbreConLaLlaveEnElCerrojo() throws Exception {
        Partida partida =
                new Partida(
                        cargar(
                                "########",
                                "#L   W.#",
                                "########", //
                                "        ",
                                "  K@$   ",
                                "        "));
        assertEquals('W', terrenoEn(partida, 1, 5));
        assertFalse(partida.mover(DERECHA), "La caja no entra al muro cerrado");
        assertTrue(partida.mover(IZQUIERDA)); // la llave queda en el cerrojo
        assertEquals('w', terrenoEn(partida, 1, 5));
        assertTrue(partida.mover(DERECHA));
        assertTrue(partida.mover(DERECHA)); // la caja entra al muro abierto
        assertTrue(partida.mover(DERECHA)); // y llega al destino
        assertTrue(partida.esVictoria());
        assertEquals(4, partida.getMovimientos());
    }

    @Test
    void laLlaveQueSaleDelCerrojoNoPuedeEntrarAlMuro() throws Exception {
        Partida partida =
                new Partida(
                        cargar(
                                "########",
                                "#.  WL #",
                                "########", //
                                "        ",
                                "  $  K@ ",
                                "        "));
        assertEquals('w', terrenoEn(partida, 1, 4));
        assertFalse(partida.mover(IZQUIERDA));
        assertEquals('K', objetoEn(partida, 1, 5));
        assertEquals('w', terrenoEn(partida, 1, 4));
        assertEquals(0, partida.getMovimientos());
    }

    @Test
    void cajaFragilSeRompeAlCuartoEmpujeYUndoLaRecupera() throws Exception {
        ControladorJuego juego =
                new ControladorJuego(
                        cargar(
                                "#########",
                                "#      .#",
                                "#########", //
                                "         ",
                                " @F      ",
                                "         "));
        for (int i = 0; i < 3; i++) assertTrue(juego.mover(DERECHA));
        assertEquals('1', juego.getTableroDTO().objetos().split("\n")[1].charAt(5));
        assertTrue(juego.mover(DERECHA));
        assertFalse(juego.esVictoria());
        assertFalse(juego.mover(DERECHA), "Con la caja rota el nivel termina");
        assertTrue(juego.deshacer());
        assertEquals('4', juego.getTableroDTO().objetos().split("\n")[1].charAt(2));
        assertTrue(juego.mover(DERECHA));
    }

    @Test
    void cajaFragilLlegaConTresEmpujes() throws Exception {
        Partida partida =
                new Partida(
                        cargar(
                                "#######", "#    .#", "#######", //
                                "       ", " @F    ", "       "));
        for (int i = 0; i < 3; i++) assertTrue(partida.mover(DERECHA));
        assertTrue(partida.esVictoria());
    }

    @Test
    void validaLlavesContraCerrojosYMurosSinCerrojo() {
        assertThrows(
                IllegalArgumentException.class,
                () -> cargar("######", "#L  .#", "######", "      ", " @ $ ", "      "));
        assertThrows(
                IllegalArgumentException.class,
                () -> cargar("######", "# W .#", "######", "      ", "  @$  ", "      "));
    }

    @Test
    void laLlaveQueCaeEnUnAgujeroEsDerrotaYUndoLaRecupera() throws Exception {
        ControladorJuego juego =
                new ControladorJuego(
                        cargar(
                                "########",
                                "#L  O .#",
                                "#      #",
                                "########", //
                                "        ",
                                "  @K    ",
                                "  $  $  ",
                                "        "));
        assertTrue(juego.mover(DERECHA));
        assertTrue(juego.esDerrota());
        assertEquals(' ', juego.getTableroDTO().terreno().split("\n")[1].charAt(4));
        assertFalse(juego.mover(IZQUIERDA), "Con derrota no se puede seguir");
        assertTrue(juego.deshacer());
        assertFalse(juego.esDerrota());
        assertEquals('O', juego.getTableroDTO().terreno().split("\n")[1].charAt(4));
        assertEquals('K', juego.getTableroDTO().objetos().split("\n")[1].charAt(3));
    }

    @Test
    void laCajaPasaPorElPortalYSaleEnLaMismaDireccion() throws Exception {
        Partida partida =
                new Partida(
                        cargar(
                                "#########",
                                "#  1 1. #",
                                "#########", //
                                "         ",
                                " @$      ",
                                "         "));
        assertTrue(partida.mover(DERECHA));
        assertEquals('$', objetoEn(partida, 1, 6));
        assertTrue(partida.esVictoria());
    }

    @Test
    void rechazaPortalSinParejaYCiclosDeCintas() {
        assertThrows(
                IllegalArgumentException.class,
                () -> cargar("#######", "#1  . #", "#######", "       ", "    $@ ", "       "));
        var ciclo =
                assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                cargar(
                                        "#######", "#>v . #", "#^<   #", "#######", //
                                        "       ", "       ", "    $@ ", "       "));
        assertTrue(ciclo.getMessage().contains("ciclo"));
    }
}
