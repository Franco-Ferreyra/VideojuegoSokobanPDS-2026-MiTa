package temuexpress.recursos;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

/** El agua de una cascada pintada en el fondo: vetas que bajan y espuma abajo. */
final class CapaCascada implements Capa {
    private static final int LARGO_VETA = 60;
    private static final int GOTAS = 26;
    private static final int ESPUMA = 0xF0F5FF;

    private final int x0;
    private final int x1;
    private final int y0;
    private final int y1;
    private final double[][] vetas;
    private final double[] gotaX;
    private List<int[]> agua;

    /** Busca el agua (píxeles claros) dentro del rectángulo (x0, y0) - (x1, y1). */
    CapaCascada(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        SplittableRandom azar = new SplittableRandom(3);
        vetas = new double[x1 - x0][LARGO_VETA];
        for (double[] columna : vetas) {
            double v = azar.nextDouble();
            for (int i = 0; i < LARGO_VETA; i++) {
                v = 0.7 * v + 0.3 * azar.nextDouble();
                columna[i] = v;
            }
        }
        gotaX = new double[GOTAS];
        for (int i = 0; i < GOTAS; i++) gotaX[i] = x0 - 4 + azar.nextDouble() * (x1 - x0);
    }

    @Override
    public void pintar(Cuadro c) {
        if (agua == null) agua = buscarAgua(c);
        int t = c.numero();
        for (int[] p : agua) {
            double v = vetas[p[0] - x0][Math.floorMod(p[1] - 2 * t, LARGO_VETA)];
            c.fijar(p[0], p[1], Cuadro.brillo(c.base(p[0], p[1]), 0.82 + 0.3 * v));
        }
        for (int i = 0; i < GOTAS; i++) {
            double ciclo = (t * 4 / 120.0 + i / (double) GOTAS) % 1;
            double x = gotaX[i] + Math.sin(i + ciclo * 6) * 3;
            double y = y1 - 1 - ciclo * 16;
            c.mezclar((int) x, (int) y, ESPUMA, 0.7 * (1 - ciclo));
        }
    }

    private List<int[]> buscarAgua(Cuadro c) {
        List<int[]> res = new ArrayList<>();
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                int rgb = c.base(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                if (r + g + b > 470 && b >= r - 10) res.add(new int[] {x, y});
            }
        }
        return res;
    }
}
