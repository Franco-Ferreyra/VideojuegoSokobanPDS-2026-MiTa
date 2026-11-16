package temuexpress.recursos;

import java.util.SplittableRandom;

/** Copos que caen con un poco de viento. No caen sobre el tablero ni los paneles del HUD. */
final class CapaNieve implements Capa {
    private static final int BLANCO = 0xFFFFFF;

    private final int desde;
    private final int alto;
    private final double[] x;
    private final double[] y;
    private final double[] v;
    private final double[] fase;
    private final int[] tam;

    CapaNieve(int cantidad, int desde, int hasta) {
        this.desde = desde;
        this.alto = hasta - desde;
        x = new double[cantidad];
        y = new double[cantidad];
        v = new double[cantidad];
        fase = new double[cantidad];
        tam = new int[cantidad];
        SplittableRandom azar = new SplittableRandom(cantidad);
        for (int i = 0; i < cantidad; i++) {
            x[i] = azar.nextDouble() * Cuadro.ANCHO;
            y[i] = azar.nextDouble() * alto;
            v[i] = 0.4 + azar.nextDouble() * 0.7;
            fase[i] = azar.nextDouble() * 2 * Math.PI;
            tam[i] = azar.nextDouble() < 0.45 ? 1 : 2;
        }
    }

    @Override
    public void pintar(Cuadro c) {
        int t = c.numero();
        for (int i = 0; i < x.length; i++) {
            int py = desde + (int) ((y[i] + t * v[i]) % alto);
            double viento = 3 * Math.sin(fase[i] + 2 * Math.PI * t / 40.0);
            int px = (int) Math.floorMod((long) (x[i] + viento), Cuadro.ANCHO);
            double opacidad = tam[i] == 2 ? 0.9 : 0.75;
            for (int dx = 0; dx < tam[i]; dx++) {
                for (int dy = 0; dy < tam[i]; dy++) {
                    if (py + dy < desde + alto) c.mezclar(px + dx, py + dy, BLANCO, opacidad);
                }
            }
        }
    }
}
