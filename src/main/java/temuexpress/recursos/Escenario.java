package temuexpress.recursos;

import java.awt.Image;

/** Producto: fondo (con el HUD dibujado) y casillas propias de la región. */
public interface Escenario {
    Image getFondo();

    Image getSpritePared();

    Image getSpritePiso();

    Image getSpriteDestino();

    Image getSpriteMuroCerrado();

    Image getSpriteMuroAbierto();

    /** El fondo con el cielo animado propio de la región. */
    AnimacionFondo getAnimacion();
}
