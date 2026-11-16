package temuexpress.recursos;

import java.awt.Image;
import temuexpress.modelo.nucleo.Direccion;

/** Producto: el repartidor de la región, con su color. */
public interface AspectoRepartidor {
    Image getSprite(Direccion dir);

    /** Se muestra en la pantalla de derrota. */
    Image getEsqueleto();
}
