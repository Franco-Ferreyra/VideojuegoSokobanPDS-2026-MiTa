package temuexpress.recursos;

import java.util.ArrayList;
import java.util.List;

/**
 * Las luces de Las Vegas sobre el fondo de EEUU: lamparitas del cartel con luz que corre, "LAS
 * VEGAS" que late, el neón TEMU que titila, ventanas que se prenden y apagan, estrellas y la luz de
 * la torre. Busca cada cosa por su color dentro de su zona del fondo.
 */
final class CapaLasVegas implements Capa {
    private static final int PERIODO = 120;
    private static final int ROJO = 0xFF3C3C;

    private List<int[]> focos;
    private List<int[]> letras;
    private List<int[]> neon;
    private List<int[]> ventanas;
    private List<int[]> estrellas;

    @Override
    public void pintar(Cuadro c) {
        if (focos == null) buscar(c);
        int t = c.numero();
        for (int[] p : focos) {
            boolean prendido = Math.floorMod(p[0] / 3 + p[1] / 3 - t, 4) < 2;
            c.fijar(p[0], p[1], Cuadro.brillo(c.base(p[0], p[1]), prendido ? 1.35 : 0.6));
        }
        double k = 0.85 + 0.25 * (0.5 + 0.5 * Math.sin(2 * Math.PI * 4 * t / PERIODO));
        for (int[] p : letras) c.fijar(p[0], p[1], Cuadro.brillo(c.base(p[0], p[1]), k));
        int m = Math.floorMod(t, 40);
        boolean apagado = m == 6 || m == 8 || m == 9 || m == 27;
        for (int[] p : neon) {
            c.fijar(p[0], p[1], Cuadro.brillo(c.base(p[0], p[1]), apagado ? 0.35 : 1.15));
        }
        for (int[] p : ventanas) {
            int celda = (p[0] / 3) * 31 + (p[1] / 3) * 17;
            boolean prendida = azar(celda * 1000 + t / 12) > 0.25;
            c.fijar(p[0], p[1], Cuadro.brillo(c.base(p[0], p[1]), prendida ? 1.1 : 0.35));
        }
        for (int[] p : estrellas) {
            double fase = p[0] * 7 + p[1] * 13;
            double e = 0.55 + 0.45 * (0.5 + 0.5 * Math.sin(2 * Math.PI * 3 * t / PERIODO + fase));
            c.fijar(p[0], p[1], Cuadro.brillo(c.base(p[0], p[1]), e));
        }
        if ((t / 8) % 2 == 0) {
            for (int dx = -1; dx <= 1; dx++) c.mezclar(343 + dx, 45, ROJO, 0.9);
        }
    }

    /** Número "al azar" entre 0 y 1, siempre el mismo para la misma semilla. */
    private static double azar(int semilla) {
        int h = semilla * 0x9E3779B1;
        h ^= h >>> 15;
        h *= 0x85EBCA77;
        h ^= h >>> 13;
        return (h & 0xFFFF) / 65535.0;
    }

    private void buscar(Cuadro c) {
        focos = new ArrayList<>();
        letras = new ArrayList<>();
        neon = new ArrayList<>();
        ventanas = new ArrayList<>();
        estrellas = new ArrayList<>();
        recorrer(
                c,
                140,
                106,
                294,
                176,
                (r, g, b, p) -> {
                    if (r > 190 && g > 140 && b < 120) focos.add(p);
                    else if (r > 170 && g < 90 && b < 110) letras.add(p);
                });
        recorrer(
                c,
                305,
                120,
                326,
                176,
                (r, g, b, p) -> {
                    if (r + g + b > 380) neon.add(p);
                });
        int[][] edificios = {{30, 108, 64, 178}, {348, 106, 396, 178}, {150, 150, 175, 178}};
        for (int[] e : edificios) {
            recorrer(
                    c,
                    e[0],
                    e[1],
                    e[2],
                    e[3],
                    (r, g, b, p) -> {
                        int max = Math.max(r, Math.max(g, b));
                        int min = Math.min(r, Math.min(g, b));
                        if (r + g + b > 330 && max - min > 60) ventanas.add(p);
                    });
        }
        recorrer(
                c,
                20,
                42,
                412,
                105,
                (r, g, b, p) -> {
                    if (r + g + b > 600) estrellas.add(p);
                });
    }

    @FunctionalInterface
    private interface Revisar {
        void pixel(int r, int g, int b, int[] p);
    }

    private static void recorrer(Cuadro c, int x0, int y0, int x1, int y1, Revisar revisar) {
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                int rgb = c.base(x, y);
                revisar.pixel((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, new int[] {x, y});
            }
        }
    }
}
