package temuexpress.recursos;

/** Helicópteros que patrullan de un lado a otro con la hélice girando. */
final class CapaHelicopteros implements Capa {
    private static final int PERIODO = 120;
    private static final int HELICE = 0xC8C8D2;

    /** Dónde estaba cada helicóptero pintado en el fondo, y cuánto se mueve. */
    private static final int[][] LUGARES = {{343, 76, 18}, {396, 99, 12}, {205, 110, 10}};

    private static final double[] FASES = {0.0, 2.0, 4.0};

    private final Sprite[] helicopteros = new Sprite[LUGARES.length];

    CapaHelicopteros(String carpeta) {
        for (int i = 0; i < LUGARES.length; i++) {
            helicopteros[i] = Sprite.leer(carpeta + "/anim/helicoptero_" + (i + 1) + ".png");
        }
    }

    @Override
    public void pintar(Cuadro c) {
        int t = c.numero();
        double a = 2 * Math.PI * t / PERIODO;
        for (int i = 0; i < LUGARES.length; i++) {
            Sprite h = helicopteros[i];
            double x = LUGARES[i][0] + LUGARES[i][2] * Math.sin(a + FASES[i]);
            double y = LUGARES[i][1] + 2 * Math.sin(3 * a + FASES[i]);
            c.pegar(h, x, y);
            int largo = h.ancho() / 2 + (t % 2 == 0 ? -3 : 3);
            int centro = (int) (x + h.ancho() / 2.0);
            for (int dx = -largo / 2; dx <= largo / 2; dx++) {
                c.mezclar(centro + dx, (int) y - 1, HELICE, 0.8);
            }
        }
    }
}
