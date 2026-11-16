package temuexpress.vista;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Portada del juego: el logo flotando con un brillo que lo cruza cada tanto, y un cartel chico con
 * el equipo y "ENTER para empezar" titilando. Se anima con el reloj del fondo (15 cuadros por
 * segundo).
 */
class PantallaInicio implements Pantalla {

    private static final int Y_LOGO = 44;
    private static final int CENTRO_X = Disposicion.ANCHO / 2;

    /** El logo sube y baja 2 píxeles en este tiempo. */
    private static final double PERIODO_FLOTAR_MS = 2500;

    /** Cada cuánto pasa el brillo y cuánto tarda en cruzar. */
    private static final long PERIODO_BRILLO_MS = 4000;

    private static final long DURACION_BRILLO_MS = 1200;

    /** "ENTER para empezar": prendido 650 ms de cada segundo. */
    private static final long PRENDIDO_MS = 650;

    private static final String EQUIPO = "Los DesubiAmericanos";
    private static final String PIE = "ENTER para empezar";
    private static final int MARGEN = 20;
    private static final int ALTO_CARTEL = 52;
    private static final int Y_CARTEL = 240;

    /** Sombra extra para que el logo resalte sobre la escena. */
    private static final Color SOMBRA_PORTADA = new Color(0, 0, 0, 110);

    private static final Color BRILLO = new Color(255, 255, 230);

    private final long inicio = System.currentTimeMillis();
    private BufferedImage logoConBrillo;

    @Override
    public void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites) {
        long ms = System.currentTimeMillis() - inicio;
        g.setColor(SOMBRA_PORTADA);
        g.fillRect(
                0,
                Disposicion.ALTO_BARRA,
                Disposicion.ANCHO,
                Disposicion.ALTO - Disposicion.ALTO_BARRA);
        Cartel cartel = cartelAjustado(g);
        cartel.dibujarFondo(g, estilo);
        dibujarLogo(g, estilo, sprites.get("logo"), ms);
        Texto.centro(g, EQUIPO, CENTRO_X, cartel.zona().y + 22, Texto.CUERPO, Texto.BLANCO);
        if (ms % 1000 < PRENDIDO_MS) cartel.pie(g, PIE, estilo);
    }

    /** El cartel tiene el ancho justo del texto más largo, con un margen a cada lado. */
    private static Cartel cartelAjustado(Graphics2D g) {
        FontMetrics fm = g.getFontMetrics(Texto.CUERPO);
        int ancho = Math.max(fm.stringWidth(EQUIPO), fm.stringWidth(PIE)) + 2 * MARGEN;
        return new Cartel(new Rectangle(CENTRO_X - ancho / 2, Y_CARTEL, ancho, ALTO_CARTEL), true);
    }

    private void dibujarLogo(Graphics2D g, EstiloRegion estilo, Image logo, long ms) {
        if (logo == null) {
            Texto.centro(g, "特穆快递", CENTRO_X, 110, Texto.TITULO, Texto.ROJO);
            Texto.centro(g, "TeMU EXPRESS", CENTRO_X, 140, Texto.TITULO, estilo.valor());
            return;
        }
        int y = Y_LOGO + (int) Math.round(2 * Math.sin(2 * Math.PI * ms / PERIODO_FLOTAR_MS));
        Image dibujo = logo;
        long fase = ms % PERIODO_BRILLO_MS;
        if (fase < DURACION_BRILLO_MS && logo instanceof BufferedImage) {
            dibujo = conBrillo((BufferedImage) logo, fase / (double) DURACION_BRILLO_MS);
        }
        g.drawImage(dibujo, CENTRO_X - logo.getWidth(null) / 2, y, null);
    }

    /** Copia del logo con un reflejo en diagonal; avance va de 0 (izquierda) a 1 (derecha). */
    private BufferedImage conBrillo(BufferedImage logo, double avance) {
        int w = logo.getWidth();
        int h = logo.getHeight();
        if (logoConBrillo == null || logoConBrillo.getWidth() != w) {
            logoConBrillo = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g = logoConBrillo.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(logo, 0, 0, null);
        // SrcAtop: el reflejo solo queda donde el logo no es transparente.
        int x = (int) (avance * (w + h)) - h;
        g.setColor(BRILLO);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.6f));
        g.fill(banda(x, 18, h));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.45f));
        g.fill(banda(x + 24, 6, h));
        g.dispose();
        return logoConBrillo;
    }

    private static Polygon banda(int x, int ancho, int alto) {
        return new Polygon(
                new int[] {x, x + ancho, x + ancho + alto, x + alto},
                new int[] {alto, alto, 0, 0},
                4);
    }
}
