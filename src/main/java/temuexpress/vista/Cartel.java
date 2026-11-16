package temuexpress.vista;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Cartel con el estilo de los paneles del boceto: oscurece la escena, dibuja el panel y escribe
 * título, líneas y una indicación al pie. Lo usan las pantallas por composición.
 */
final class Cartel {

    private static final Color SOMBRA = new Color(0, 0, 0, 150);
    private static final int INTERLINEA = 16;
    private static final int INTERLINEA_MINIMA = 13;
    private static final int SEPARACION_PARRAFO = 6;

    /** Lugar que queda debajo del texto para el pie ("ENTER para seguir"). */
    private static final int ESPACIO_PIE = 34;

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
            g.fillRect(
                    0,
                    Disposicion.ALTO_BARRA,
                    Disposicion.ANCHO,
                    Disposicion.ALTO - Disposicion.ALTO_BARRA);
        }
        estilo.dibujarPanel(g, zona);
    }

    void titulo(Graphics2D g, String texto, EstiloRegion estilo) {
        Texto.centro(g, texto, centroX(), zona.y + 30, Texto.TITULO, estilo.valor());
    }

    /**
     * Escribe las líneas centradas desde la altura y, partiendo las largas para que no se salgan
     * del cartel; devuelve dónde terminó.
     */
    int lineas(Graphics2D g, String[] lineas, int y) {
        for (String linea : lineas) {
            for (String renglon : Texto.partir(g, linea, Texto.CUERPO, zona.width - 24)) {
                Texto.centro(g, renglon, centroX(), y, Texto.CUERPO, Texto.BLANCO);
                y += INTERLINEA;
            }
        }
        return y;
    }

    /** Alto que ocupan esas líneas como párrafos dentro de un cartel de ese ancho. */
    static int altoParrafos(Graphics2D g, String[] lineas, int anchoCartel) {
        int renglones = 0;
        for (String linea : lineas) {
            renglones += Texto.partir(g, linea, Texto.CUERPO, anchoCartel - 24).size();
        }
        return renglones * INTERLINEA + Math.max(0, lineas.length - 1) * SEPARACION_PARRAFO;
    }

    /**
     * Cartel de ese ancho, con el alto justo para el texto (más el espacio de arriba para el título
     * y el de abajo para el pie), centrado en la pantalla debajo de la barra.
     */
    static Cartel ajustado(int ancho, int altoTexto, int arriba, boolean oscurece) {
        int disponible = Disposicion.ALTO - Disposicion.ALTO_BARRA - 8;
        int alto = Math.min(disponible, arriba + altoTexto + ESPACIO_PIE);
        int x = (Disposicion.ANCHO - ancho) / 2;
        int y = Disposicion.ALTO_BARRA + (Disposicion.ALTO - Disposicion.ALTO_BARRA - alto) / 2;
        return new Cartel(new Rectangle(x, y, ancho, alto), oscurece);
    }

    /**
     * Como {@link #lineas}, pero cada línea es un párrafo: deja un pequeño espacio entre párrafos
     * y, si el texto no entra antes del pie, junta los renglones para que nunca se pise con él.
     */
    void parrafos(Graphics2D g, String[] lineas, int y) {
        List<List<String>> partidos = new ArrayList<>();
        int renglones = 0;
        for (String linea : lineas) {
            List<String> r = Texto.partir(g, linea, Texto.CUERPO, zona.width - 24);
            partidos.add(r);
            renglones += r.size();
        }
        int espacio = zona.y + zona.height - 30 - y;
        int separacion = SEPARACION_PARRAFO;
        int interlinea = INTERLINEA;
        while (renglones * interlinea + (partidos.size() - 1) * separacion > espacio
                && interlinea > INTERLINEA_MINIMA) {
            interlinea--;
            separacion = Math.max(0, separacion - 1);
        }
        for (List<String> parrafo : partidos) {
            for (String renglon : parrafo) {
                Texto.centro(g, renglon, centroX(), y, Texto.CUERPO, Texto.BLANCO);
                y += interlinea;
            }
            y += separacion;
        }
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
