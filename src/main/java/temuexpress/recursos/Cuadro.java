package temuexpress.recursos;

/**
 * Un cuadro de la animación del fondo: los píxeles que se están pintando, el fondo original y qué
 * píxeles son cielo (lo que se pega en el cielo pasa por detrás de edificios y montañas).
 */
final class Cuadro {
    static final int ANCHO = AnimacionFondo.ANCHO;
    static final int ALTO = AnimacionFondo.ALTO;

    private final int[] pixeles;
    private final int[] base;
    private final boolean[] cielo;
    private final int numero;

    Cuadro(int[] pixeles, int[] base, boolean[] cielo, int numero) {
        this.pixeles = pixeles;
        this.base = base;
        this.cielo = cielo;
        this.numero = numero;
    }

    int numero() {
        return numero;
    }

    static boolean adentro(int x, int y) {
        return x >= 0 && x < ANCHO && y >= 0 && y < ALTO;
    }

    boolean esCielo(int x, int y) {
        return adentro(x, y) && cielo[y * ANCHO + x];
    }

    /** Color del fondo original (sin animar) en ese píxel. */
    int base(int x, int y) {
        return base[y * ANCHO + x];
    }

    void fijar(int x, int y, int rgb) {
        if (adentro(x, y)) pixeles[y * ANCHO + x] = rgb;
    }

    /** Mezcla un color encima con opacidad {@code a} (0 a 1). */
    void mezclar(int x, int y, int rgb, double a) {
        if (!adentro(x, y)) return;
        int i = y * ANCHO + x;
        pixeles[i] = combinar(rgb, pixeles[i], a);
    }

    /** Pega el sprite solo sobre el cielo. */
    void pegar(Sprite sprite, double x, double y) {
        int x0 = (int) Math.floor(x);
        int y0 = (int) Math.floor(y);
        for (int j = 0; j < sprite.alto(); j++) {
            for (int i = 0; i < sprite.ancho(); i++) {
                int argb = sprite.argb(i, j);
                int alfa = argb >>> 24;
                if (alfa == 0 || !esCielo(x0 + i, y0 + j)) continue;
                mezclar(x0 + i, y0 + j, argb & 0xFFFFFF, alfa / 255.0);
            }
        }
    }

    /** Multiplica el brillo de un color (k mayor que 1 aclara, menor que 1 oscurece). */
    static int brillo(int rgb, double k) {
        int r = canal((rgb >> 16) & 0xFF, k);
        int g = canal((rgb >> 8) & 0xFF, k);
        int b = canal(rgb & 0xFF, k);
        return (r << 16) | (g << 8) | b;
    }

    private static int canal(int v, double k) {
        return Math.max(0, Math.min(255, (int) (v * k)));
    }

    private static int combinar(int arriba, int abajo, double a) {
        int r = (int) (((arriba >> 16) & 0xFF) * a + ((abajo >> 16) & 0xFF) * (1 - a));
        int g = (int) (((arriba >> 8) & 0xFF) * a + ((abajo >> 8) & 0xFF) * (1 - a));
        int b = (int) ((arriba & 0xFF) * a + (abajo & 0xFF) * (1 - a));
        return (r << 16) | (g << 8) | b;
    }
}
