package vista;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Cartel con el estilo de los paneles del boceto: oscurece la escena,
 * dibuja el panel y escribe título, líneas y una indicación al pie.
 * Lo usan las pantallas por composición.
 */
final class Cartel {

    private static final Color SOMBRA = new Color(0, 0, 0, 150);
    private static final int INTERLINEA = 16;

    private final Rectangle zona;
    private final boolean oscurece;

    /** Si oscurece, tapa la escena; si no, deja ver el tablero alrededor. */
    Cartel(Rectangle zona, boolean oscurece) {
        this.zona = zona;
        this.oscurece = oscurece;
    }

    void dibujarFondo(Graphics2D g, EstiloRegion estilo) {
        if (oscurece) {
            g.setColor(SOMBRA);
            g.fillRect(0, Disposicion.ALTO_BARRA, Disposicion.ANCHO,
                    Disposicion.ALTO - Disposicion.ALTO_BARRA);
        }
        estilo.dibujarPanel(g, zona);
    }

    void titulo(Graphics2D g, String texto, EstiloRegion estilo) {
        Texto.centro(g, texto, centroX(), zona.y + 30, Texto.TITULO, estilo.valor());
    }

    /** Escribe las líneas centradas desde la altura y; devuelve dónde terminó. */
    int lineas(Graphics2D g, String[] lineas, int y) {
        for (String linea : lineas) {
            Texto.centro(g, linea, centroX(), y, Texto.CUERPO, Texto.BLANCO);
            y += INTERLINEA;
        }
        return y;
    }

    void pie(Graphics2D g, String texto, EstiloRegion estilo) {
        Texto.centro(g, texto, centroX(), zona.y + zona.height - 12, Texto.CUERPO, estilo.undo());
    }

    int centroX() {
        return zona.x + zona.width / 2;
    }

    Rectangle zona() {
        return zona;
    }
}
