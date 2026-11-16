package temuexpress.vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JPanel;
import javax.swing.Timer;
import temuexpress.recursos.AnimacionFondo;

/**
 * Dibuja todo en una imagen del tamaño del boceto (432x324) y la escala a la ventana sin suavizar.
 * Pasa las teclas y los clics al OyenteVista. Orden de dibujo: fondo del boceto, tablero, valores
 * del HUD y pantalla.
 */
class PanelJuego extends JPanel {

    private static final long serialVersionUID = 1L;

    // transient: el estado de la vista no se serializa (JPanel es Serializable).
    private final transient BufferedImage lienzo =
            new BufferedImage(Disposicion.ANCHO, Disposicion.ALTO, BufferedImage.TYPE_INT_RGB);

    private final transient VistaTablero tablero = new VistaTablero();
    private final transient VistaHUD hud = new VistaHUD();
    private transient Pantalla pantalla = new PantallaInicio();
    private transient HojaSprites sprites = new HojaSprites();
    private transient EstiloRegion estilo = EstiloRegion.desde(null);
    private transient OyenteVista oyente;

    /** Frase del repartidor (globo) y el reloj que la borra. */
    private static final Rectangle GLOBO = new Rectangle(76, 146, 280, 34);

    private static final int DURACION_GLOBO_MS = 2500;
    private transient String dialogo;
    private transient Timer relojDialogo;

    /** Cielo animado de la región: se pinta un cuadro nuevo 15 veces por segundo. */
    private transient AnimacionFondo animacion;

    private transient BufferedImage fondoAnimado;
    private transient int[] pixelesFondo;
    private transient Timer relojFondo;
    private transient int cuadro;

    PanelJuego() {
        setPreferredSize(
                new Dimension(
                        Disposicion.ANCHO * Disposicion.ESCALA_INICIAL,
                        Disposicion.ALTO * Disposicion.ESCALA_INICIAL));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new Teclado());
        addMouseListener(new Mouse());
    }

    private void borrarDialogo() {
        dialogo = null;
        repaint();
    }

    void mostrarDialogo(String frase) {
        dialogo = frase;
        if (relojDialogo == null) {
            relojDialogo = new Timer(DURACION_GLOBO_MS, e -> borrarDialogo());
            relojDialogo.setRepeats(false);
        }
        relojDialogo.restart();
        repaint();
    }

    void setOyente(OyenteVista oyente) {
        this.oyente = oyente;
    }

    void setSprites(HojaSprites sprites) {
        this.sprites = sprites;
        Image fondo = sprites.get("fondo");
        estilo = EstiloRegion.desde(fondo instanceof BufferedImage ? (BufferedImage) fondo : null);
    }

    void setPantalla(Pantalla pantalla) {
        this.pantalla = pantalla;
    }

    VistaTablero tablero() {
        return tablero;
    }

    VistaHUD hud() {
        return hud;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarLienzo();
        int escala = escala();
        int w = Disposicion.ANCHO * escala;
        int h = Disposicion.ALTO * escala;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(lienzo, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, null);
    }

    private void dibujarLienzo() {
        Graphics2D g = lienzo.createGraphics();
        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        dibujarFondo(g);
        tablero.dibujar(g, sprites);
        hud.dibujar(g, estilo);
        pantalla.dibujar(g, estilo, sprites);
        if (dialogo != null && pantalla.permiteMover()) dibujarDialogo(g);
        g.dispose();
    }

    private void dibujarDialogo(Graphics2D g) {
        estilo.dibujarPanel(g, GLOBO);
        int y = GLOBO.y + 14;
        for (String renglon : Texto.partir(g, dialogo, Texto.CUERPO, GLOBO.width - 16)) {
            Texto.centro(g, renglon, GLOBO.x + GLOBO.width / 2, y, Texto.CUERPO, estilo.valor());
            y += 13;
        }
    }

    void setAnimacion(AnimacionFondo animacion) {
        this.animacion = animacion;
        if (animacion == null) {
            if (relojFondo != null) relojFondo.stop();
            return;
        }
        if (fondoAnimado == null) {
            fondoAnimado =
                    new BufferedImage(
                            AnimacionFondo.ANCHO, AnimacionFondo.ALTO, BufferedImage.TYPE_INT_RGB);
            pixelesFondo = ((DataBufferInt) fondoAnimado.getRaster().getDataBuffer()).getData();
        }
        if (relojFondo == null) {
            relojFondo =
                    new Timer(
                            1000 / AnimacionFondo.CUADROS_POR_SEGUNDO,
                            e -> {
                                cuadro++;
                                repaint();
                            });
        }
        relojFondo.start();
    }

    private void dibujarFondo(Graphics2D g) {
        if (animacion != null) {
            animacion.pintar(pixelesFondo, cuadro);
            g.drawImage(fondoAnimado, 0, 0, null);
            return;
        }
        Image fondo = sprites.get("fondo");
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, Disposicion.ANCHO, Disposicion.ALTO, null);
        } else {
            g.setColor(estilo.panel());
            g.fillRect(0, 0, Disposicion.ANCHO, Disposicion.ALTO);
        }
    }

    /** Escala entera más grande que entra en el panel (pixel art sin deformar). */
    private int escala() {
        return Math.max(
                1, Math.min(getWidth() / Disposicion.ANCHO, getHeight() / Disposicion.ALTO));
    }

    private class Teclado extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (oyente == null) {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (pantalla.permiteContinuar()) {
                    oyente.alContinuar();
                }
                return;
            }
            String direccion = direccion(e.getKeyCode());
            if (direccion != null && pantalla.permiteMover()) {
                tablero.setDireccionJugador(direccion);
                oyente.alPresionarFlecha(direccion);
            }
        }

        private String direccion(int tecla) {
            switch (tecla) {
                case KeyEvent.VK_UP:
                    return "ARRIBA";
                case KeyEvent.VK_DOWN:
                    return "ABAJO";
                case KeyEvent.VK_LEFT:
                    return "IZQUIERDA";
                case KeyEvent.VK_RIGHT:
                    return "DERECHA";
                default:
                    return null;
            }
        }
    }

    private class Mouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            requestFocusInWindow();
            if (oyente == null) {
                return;
            }
            int escala = escala();
            int x = (e.getX() - (getWidth() - Disposicion.ANCHO * escala) / 2) / escala;
            int y = (e.getY() - (getHeight() - Disposicion.ALTO * escala) / 2) / escala;
            if (pantalla.botonesActivos() && Disposicion.BOTON_UNDO.contains(x, y)) {
                oyente.alPresionarUndo();
            } else if (pantalla.botonesActivos() && Disposicion.BOTON_REINICIAR.contains(x, y)) {
                oyente.alPresionarReiniciar();
            } else if (pantalla.permiteContinuar()) {
                oyente.alContinuar();
            }
        }
    }
}
