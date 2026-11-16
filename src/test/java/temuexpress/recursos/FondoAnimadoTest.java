package temuexpress.recursos;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class FondoAnimadoTest {

    private static List<Escenario> escenarios() {
        return List.of(
                new EscenarioChina(),
                new EscenarioEEUU(),
                new EscenarioAlemania(),
                new EscenarioSudamerica("arg_chile"),
                new EscenarioSudamerica("ecuador_colombia"),
                new EscenarioSudamerica("peru_venezuela"));
    }

    @Test
    void cadaRegionTieneElCieloEnMovimiento() {
        for (Escenario escenario : escenarios()) {
            AnimacionFondo animacion = escenario.getAnimacion();
            int[] antes = new int[AnimacionFondo.ANCHO * AnimacionFondo.ALTO];
            int[] despues = new int[antes.length];
            animacion.pintar(antes, 0);
            for (int t = 1; t <= 30; t++) animacion.pintar(despues, t);
            assertFalse(Arrays.equals(antes, despues), escenario.getClass().getSimpleName());
        }
    }

    @Test
    void elTableroYElHudNoSeAniman() {
        int desde = 190 * AnimacionFondo.ANCHO;
        for (Escenario escenario : escenarios()) {
            AnimacionFondo animacion = escenario.getAnimacion();
            int[] antes = new int[AnimacionFondo.ANCHO * AnimacionFondo.ALTO];
            int[] despues = new int[antes.length];
            animacion.pintar(antes, 0);
            animacion.pintar(despues, 45);
            assertFalse(
                    !Arrays.equals(antes, desde, antes.length, despues, desde, antes.length),
                    escenario.getClass().getSimpleName());
        }
    }
}
