package temuexpress.vista;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/** Mensaje regional al empezar un nivel. */
class PantallaMensaje implements Pantalla {

    private final Cartel cartel = new Cartel(new Rectangle(20, 70, 392, 190), true);
    private final String titulo;
    private final String[] lineas;

    PantallaMensaje(String titulo, String[] lineas) {
        this.titulo = titulo;
        this.lineas = lineas.clone();
    }

    @Override
    public void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites) {
        cartel.dibujarFondo(g, estilo);
        cartel.titulo(g, titulo, estilo);
        cartel.lineas(g, lineas, cartel.zona().y + 66);
        cartel.pie(g, "ENTER para empezar", estilo);
    }
}
