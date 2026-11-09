package temuexpress.vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/** Escribe texto en la resolución base con letra fina, como en los bocetos. */
final class Texto {

    static final Font VALOR = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    static final Font CUERPO = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    static final Font TITULO = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

    static final Color BLANCO = new Color(240, 240, 240);
    static final Color ROJO = new Color(230, 50, 50);

    private Texto() {}

    static void izquierda(Graphics2D g, String s, int x, int y, Font f, Color c) {
        g.setFont(f);
        g.setColor(c);
        g.drawString(s, x, y);
    }

    static void centro(Graphics2D g, String s, int centroX, int y, Font f, Color c) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(c);
        g.drawString(s, centroX - fm.stringWidth(s) / 2, y);
    }

    static void derecha(Graphics2D g, String s, int x, int y, Font f, Color c) {
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(c);
        g.drawString(s, x - fm.stringWidth(s), y);
    }

    /** Completa con ceros a la izquierda: 12 con 3 dígitos da "012". */
    static String ceros(int numero, int digitos) {
        StringBuilder sb = new StringBuilder(String.valueOf(Math.max(0, numero)));
        while (sb.length() < digitos) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }
}
