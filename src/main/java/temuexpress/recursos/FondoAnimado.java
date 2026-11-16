package temuexpress.recursos;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Composite: el fondo de la región más sus capas animadas. Calcula una sola vez qué píxeles son
 * cielo (los que se parecen al color más común de su fila) para que lo que vuela pase por detrás de
 * los edificios y las montañas.
 */
final class FondoAnimado implements AnimacionFondo {
    private static final int TOLERANCIA_CIELO = 45;

    private final int[] base;
    private final boolean[] cielo;
    private final List<Capa> capas;

    /** El cielo se busca entre las filas cieloDesde (incluida) y cieloHasta (excluida). */
    FondoAnimado(Image fondo, int cieloDesde, int cieloHasta, Capa... capas) {
        this.base = pixeles(fondo);
        this.cielo = calcularCielo(base, cieloDesde, cieloHasta);
        this.capas = List.of(capas);
    }

    @Override
    public void pintar(int[] destino, int cuadro) {
        System.arraycopy(base, 0, destino, 0, base.length);
        Cuadro c = new Cuadro(destino, base, cielo, cuadro);
        for (Capa capa : capas) capa.pintar(c);
    }

    private static int[] pixeles(Image fondo) {
        BufferedImage im = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
        if (fondo != null) {
            Graphics2D g = im.createGraphics();
            g.drawImage(fondo, 0, 0, ANCHO, ALTO, null);
            g.dispose();
        }
        int[] px = im.getRGB(0, 0, ANCHO, ALTO, null, 0, ANCHO);
        for (int i = 0; i < px.length; i++) px[i] &= 0xFFFFFF;
        return px;
    }

    private static boolean[] calcularCielo(int[] px, int desde, int hasta) {
        boolean[] cielo = new boolean[px.length];
        for (int y = Math.max(0, desde); y < Math.min(ALTO, hasta); y++) {
            int moda = colorMasComun(px, y);
            for (int x = 0; x < ANCHO; x++) {
                cielo[y * ANCHO + x] = distancia(px[y * ANCHO + x], moda) < TOLERANCIA_CIELO;
            }
        }
        return cielo;
    }

    /** Color más común de la fila, agrupando tonos parecidos (de a 8 por canal). */
    private static int colorMasComun(int[] px, int y) {
        Map<Integer, Integer> cuenta = new HashMap<>();
        int mejor = 0;
        int veces = 0;
        for (int x = 0; x < ANCHO; x++) {
            int c = (px[y * ANCHO + x] >> 3) & 0x1F1F1F;
            int n = cuenta.merge(c, 1, Integer::sum);
            if (n > veces) {
                veces = n;
                mejor = c;
            }
        }
        return (mejor << 3) | 0x040404;
    }

    private static int distancia(int a, int b) {
        return Math.abs(((a >> 16) & 0xFF) - ((b >> 16) & 0xFF))
                + Math.abs(((a >> 8) & 0xFF) - ((b >> 8) & 0xFF))
                + Math.abs((a & 0xFF) - (b & 0xFF));
    }
}
