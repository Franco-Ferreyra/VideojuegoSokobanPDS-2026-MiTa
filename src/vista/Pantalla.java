package vista;

import java.awt.Graphics2D;

/**
 * Lo que se ve encima del juego en cada momento (inicio, mensaje, juego,
 * resumen, derrota, final). Cada pantalla es una clase, así agregar una
 * nueva no obliga a tocar las otras (abierto/cerrado).
 */
interface Pantalla {

    void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites);

    /** Si las flechas mueven al repartidor. */
    default boolean permiteMover() {
        return false;
    }

    /** Si los botones UNDO y REINICIAR responden. */
    default boolean botonesActivos() {
        return false;
    }

    /** Si ENTER o un clic pasan a lo siguiente. */
    default boolean permiteContinuar() {
        return true;
    }
}
