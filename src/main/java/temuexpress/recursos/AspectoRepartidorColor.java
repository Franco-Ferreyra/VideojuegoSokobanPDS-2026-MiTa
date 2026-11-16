package temuexpress.recursos;

import java.awt.Image;
import java.util.Locale;
import java.util.Objects;
import temuexpress.modelo.nucleo.Direccion;

/** Repartidor que lee sus sprites de assets/img/personaje/repartidor_<color>. */
abstract class AspectoRepartidorColor implements AspectoRepartidor {
    private final String carpeta;

    AspectoRepartidorColor(String color) {
        this.carpeta = "personaje/repartidor_" + Objects.requireNonNull(color) + "/";
    }

    public Image getSprite(Direccion dir) {
        return Imagenes.leer(carpeta + dir.name().toLowerCase(Locale.ROOT) + ".png");
    }

    public Image getEsqueleto() {
        return Imagenes.leer(carpeta + "esqueleto.png");
    }
}
