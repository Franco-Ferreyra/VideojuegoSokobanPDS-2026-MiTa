package temuexpress.recursos;

/**
 * Fondo de la región con el cielo animado (nubes, objetos que cruzan, luces, nieve, agua). Lo
 * entrega el Escenario de cada fábrica regional; la vista le pide un cuadro por vez.
 */
public interface AnimacionFondo {
    /** Ancho y alto del fondo, en píxeles del boceto. */
    int ANCHO = 432;

    int ALTO = 324;

    /** Cuadros por segundo de la animación. */
    int CUADROS_POR_SEGUNDO = 15;

    /**
     * Escribe el cuadro número {@code cuadro} en {@code destino} (ANCHO x ALTO píxeles RGB, fila
     * por fila).
     */
    void pintar(int[] destino, int cuadro);
}
