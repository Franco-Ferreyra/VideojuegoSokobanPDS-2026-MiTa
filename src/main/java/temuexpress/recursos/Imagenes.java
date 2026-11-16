package temuexpress.recursos;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/** Lee imágenes de assets/img. Si falta una, devuelve null y la vista dibuja un reemplazo. */
public final class Imagenes {
    public static final String CARPETA = "assets/img/";

    private Imagenes() {}

    public static Image leer(String ruta) {
        try {
            return ImageIO.read(new File(CARPETA + ruta));
        } catch (IOException e) {
            System.err.println("No se encontro " + CARPETA + ruta);
            return null;
        }
    }
}
