package temuexpress.recursos;

import java.awt.Image;
import java.awt.image.BufferedImage;

/** Imagen chica con transparencia (nube, avión, farolito...) lista para pegar en un Cuadro. */
final class Sprite {
    private final int ancho;
    private final int alto;
    private final int[] argb;

    private Sprite(int ancho, int alto, int[] argb) {
        this.ancho = ancho;
        this.alto = alto;
        this.argb = argb;
    }

    /** Lee assets/img/ruta. Si falta, devuelve un sprite vacío (no se dibuja nada). */
    static Sprite leer(String ruta) {
        Image imagen = Imagenes.leer(ruta);
        if (!(imagen instanceof BufferedImage)) return new Sprite(0, 0, new int[0]);
        BufferedImage im = (BufferedImage) imagen;
        int w = im.getWidth();
        int h = im.getHeight();
        return new Sprite(w, h, im.getRGB(0, 0, w, h, null, 0, w));
    }

    int ancho() {
        return ancho;
    }

    int alto() {
        return alto;
    }

    int argb(int x, int y) {
        return argb[y * ancho + x];
    }
}
