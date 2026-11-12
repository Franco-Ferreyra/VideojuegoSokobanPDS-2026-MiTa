package temuexpress.vista;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/** Final del juego, después del último nivel. */
class PantallaFinal implements Pantalla {

    private final Cartel cartel = new Cartel(new Rectangle(40, 70, 352, 190), true);
    private final String[] lineas;

    PantallaFinal(String[] lineas) {
        this.lineas = lineas.clone();
    }

    @Override
    public void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites) {
        cartel.dibujarFondo(g, estilo);
        cartel.titulo(g, "¡ENTREGA COMPLETADA!", estilo);
        cartel.lineas(g, lineas, cartel.zona().y + 66);
        cartel.pie(g, "ENTER para volver al inicio", estilo);
    }
}
