package vista;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Colores de la región, tomados del propio fondo (el boceto): color del
 * panel, del borde, de los números y de los cuadraditos de Undo.
 * Así cada región se ve igual que su boceto sin escribir colores a mano.
 */
final class EstiloRegion {

    private static final Color PANEL_POR_DEFECTO = new Color(40, 20, 60);
    private static final Color BORDE_POR_DEFECTO = new Color(214, 160, 20);
    private static final Color VALOR_POR_DEFECTO = new Color(255, 206, 0);

    private final Color panel;
    private final Color borde;
    private final Color valor;
    private final Color barra;
    private final Color puntaje;
    private final Color undo;

    private EstiloRegion(Color panel, Color borde, Color valor, Color barra, Color puntaje,
            Color undo) {
        this.panel = panel;
        this.borde = borde;
        this.valor = valor;
        this.barra = barra;
        this.puntaje = puntaje;
        this.undo = undo;
    }

    static EstiloRegion desde(BufferedImage fondo) {
        if (fondo == null) {
            return new EstiloRegion(PANEL_POR_DEFECTO, BORDE_POR_DEFECTO, VALOR_POR_DEFECTO,
                    Color.BLACK, Color.WHITE, VALOR_POR_DEFECTO);
        }
        Color panel = pixel(fondo, Disposicion.MUESTRA_PANEL.x, Disposicion.MUESTRA_PANEL.y);
        Color barra = pixel(fondo, Disposicion.MUESTRA_BARRA.x, Disposicion.MUESTRA_BARRA.y);
        return new EstiloRegion(panel,
                masClaro(fondo, Disposicion.MUESTRA_BORDE),
                masDistinto(fondo, Disposicion.NIVEL, panel),
                barra,
                masDistinto(fondo, Disposicion.PUNTAJE, barra),
                masDistinto(fondo, Disposicion.MUESTRA_UNDO, panel));
    }

    Color panel() {
        return panel;
    }

    Color borde() {
        return borde;
    }

    Color valor() {
        return valor;
    }

    Color barra() {
        return barra;
    }

    Color puntaje() {
        return puntaje;
    }

    Color undo() {
        return undo;
    }

    /** Panel con el mismo aspecto que los del boceto: marco oscuro, línea clara y fondo liso. */
    void dibujarPanel(Graphics2D g, Rectangle r) {
        g.setColor(borde.darker().darker());
        g.fillRect(r.x, r.y, r.width, r.height);
        g.setColor(borde);
        g.drawRect(r.x + 2, r.y + 2, r.width - 5, r.height - 5);
        g.setColor(panel);
        g.fillRect(r.x + 3, r.y + 3, r.width - 6, r.height - 6);
    }

    private static Color pixel(BufferedImage im, int x, int y) {
        return new Color(im.getRGB(x, y));
    }

    private static Color masClaro(BufferedImage im, Rectangle zona) {
        Color mejor = BORDE_POR_DEFECTO;
        int brillo = -1;
        for (int x = zona.x; x < zona.x + zona.width; x++) {
            for (int y = zona.y; y < zona.y + zona.height; y++) {
                Color c = pixel(im, x, y);
                int b = c.getRed() + c.getGreen() + c.getBlue();
                if (b > brillo) {
                    brillo = b;
                    mejor = c;
                }
            }
        }
        return mejor;
    }

    /** El color del texto del boceto es el que más se aleja del fondo del panel. */
    private static Color masDistinto(BufferedImage im, Rectangle zona, Color referencia) {
        Color mejor = VALOR_POR_DEFECTO;
        int distancia = -1;
        for (int x = zona.x; x < zona.x + zona.width; x++) {
            for (int y = zona.y; y < zona.y + zona.height; y++) {
                Color c = pixel(im, x, y);
                int dr = c.getRed() - referencia.getRed();
                int dg = c.getGreen() - referencia.getGreen();
                int db = c.getBlue() - referencia.getBlue();
                int d = dr * dr + dg * dg + db * db;
                if (d > distancia) {
                    distancia = d;
                    mejor = c;
                }
            }
        }
        return mejor;
    }
}
