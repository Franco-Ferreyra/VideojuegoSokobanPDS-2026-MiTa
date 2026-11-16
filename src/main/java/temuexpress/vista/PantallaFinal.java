package temuexpress.vista;

import java.awt.Graphics2D;

/** Final del juego, después del último nivel. */
class PantallaFinal implements Pantalla {

    private static final int ANCHO = 352;
    private final String[] lineas;

    PantallaFinal(String[] lineas) {
        this.lineas = lineas.clone();
    }

    @Override
    public void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites) {
        Cartel cartel = Cartel.ajustado(ANCHO, Cartel.altoParrafos(g, lineas, ANCHO), 62, true);
        cartel.dibujarFondo(g, estilo);
        cartel.titulo(g, "¡ENTREGA COMPLETADA!", estilo);
        cartel.parrafos(g, lineas, cartel.zona().y + 62);
        cartel.pie(g, "ENTER para volver al inicio", estilo);
    }
}
