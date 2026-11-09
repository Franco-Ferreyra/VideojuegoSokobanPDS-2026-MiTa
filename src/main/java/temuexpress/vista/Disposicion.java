package temuexpress.vista;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Posiciones de la pantalla, medidas sobre los bocetos normalizados (fondo.png de cada región,
 * 432x324). Todas las regiones comparten esta disposición, así que la vista no depende de la
 * región.
 */
final class Disposicion {

    static final int ANCHO = 432;
    static final int ALTO = 324;
    static final int ESCALA_INICIAL = 2;

    static final int TAM_CASILLA = 16;
    static final int COLUMNAS = 13;
    static final int FILAS = 7;
    static final Point TABLERO = new Point(112, 188);

    /** Zonas del fondo donde el boceto trae números de ejemplo. */
    static final Rectangle PUNTAJE = new Rectangle(11, 19, 51, 15);

    static final Rectangle NIVEL = new Rectangle(25, 205, 27, 16);
    static final Rectangle MOVIMIENTOS = new Rectangle(25, 240, 29, 16);
    static final Rectangle EMPUJES = new Rectangle(25, 274, 29, 16);

    /** Indicador de Undo: 3 cuadraditos del boceto. */
    static final int[] UNDO_X = {334, 345, 357};

    static final int UNDO_Y = 208;
    static final int UNDO_ANCHO = 8;
    static final int UNDO_ALTO = 6;

    /** Botones dibujados en el boceto. */
    static final Rectangle BOTON_UNDO = new Rectangle(331, 222, 80, 21);

    static final Rectangle BOTON_REINICIAR = new Rectangle(331, 249, 80, 21);

    /** Puntos del fondo de donde se toman los colores del panel. */
    static final Point MUESTRA_PANEL = new Point(80, 232);

    static final Rectangle MUESTRA_BORDE = new Rectangle(14, 250, 10, 1);
    static final Rectangle MUESTRA_UNDO = new Rectangle(334, 208, 8, 6);
    static final Point MUESTRA_BARRA = new Point(70, 25);

    /** Alto de la barra de arriba (1UP, logo y región), que nunca se tapa. */
    static final int ALTO_BARRA = 37;

    private Disposicion() {}
}
