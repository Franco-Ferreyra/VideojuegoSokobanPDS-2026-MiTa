package vista;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/** Portada del juego: el logo arriba y un cartel con el equipo abajo. */
class PantallaInicio implements Pantalla {

    private static final int Y_LOGO = 44;
    /** Sombra extra para que el logo resalte sobre la escena. */
    private static final Color SOMBRA_PORTADA = new Color(0, 0, 0, 110);

    private final Cartel cartel = new Cartel(new Rectangle(106, 230, 220, 70), true);

    @Override
    public void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites) {
        g.setColor(SOMBRA_PORTADA);
        g.fillRect(0, Disposicion.ALTO_BARRA, Disposicion.ANCHO,
                Disposicion.ALTO - Disposicion.ALTO_BARRA);
        cartel.dibujarFondo(g, estilo);
        int cx = cartel.centroX();
        Image logo = sprites.get("logo");
        if (logo != null) {
            g.drawImage(logo, cx - logo.getWidth(null) / 2, Y_LOGO, null);
        } else {
            Texto.centro(g, "特穆快递", cx, 110, Texto.TITULO, Texto.ROJO);
            Texto.centro(g, "TeMU EXPRESS", cx, 140, Texto.TITULO, estilo.valor());
        }
        Texto.centro(g, "Los DesubiAmericanos", cx, cartel.zona().y + 28, Texto.CUERPO, Texto.BLANCO);
        cartel.pie(g, "ENTER para empezar", estilo);
    }
}
