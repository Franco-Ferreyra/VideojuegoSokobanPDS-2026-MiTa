package temuexpress.vista;

import java.awt.Graphics2D;

/** Mensaje regional al empezar un nivel. El cartel toma el alto justo para su texto. */
class PantallaMensaje implements Pantalla {
    private static final int ANCHO = 400;

    /** Desde el borde de arriba del cartel hasta la primera línea (debajo del título). */
    private static final int ARRIBA = 62;

    private final String titulo;
    private final String[] lineas;
    private final String pie;

    PantallaMensaje(String titulo, String[] lineas, String pie) {
        this.titulo = titulo;
        this.lineas = lineas.clone();
        this.pie = pie;
    }

    @Override
    public void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites) {
        Cartel cartel = Cartel.ajustado(ANCHO, Cartel.altoParrafos(g, lineas, ANCHO), ARRIBA, true);
        cartel.dibujarFondo(g, estilo);
        cartel.titulo(g, titulo, estilo);
        cartel.parrafos(g, lineas, cartel.zona().y + ARRIBA);
        cartel.pie(g, pie, estilo);
    }
}
