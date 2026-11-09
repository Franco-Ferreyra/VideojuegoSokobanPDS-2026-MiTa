package temuexpress.vista;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import temuexpress.modelo.dto.EventoPartidaDTO;

/**
 * Valores del HUD. Los rótulos, el logo, la región y los botones ya vienen dibujados en el fondo
 * del boceto; acá solo se escriben los números que cambian, encima del número de ejemplo del
 * boceto.
 */
class VistaHUD {

    private static final int USOS_UNDO_SEGUIDOS = 3;

    private String nivel = "";
    private int movimientos;
    private int empujes;
    private int usosUndoSeguidos;
    private int puntajeTotal;

    void actualizar(EventoPartidaDTO evento) {
        nivel = evento.getNivel();
        movimientos = evento.getMovimientos();
        empujes = evento.getEmpujes();
        usosUndoSeguidos = evento.getUsosUndoSeguidos();
    }

    void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    void dibujar(Graphics2D g, EstiloRegion estilo) {
        escribir(
                g,
                Disposicion.PUNTAJE,
                Texto.ceros(puntajeTotal, 6),
                estilo.barra(),
                estilo.puntaje());
        escribir(g, Disposicion.NIVEL, nivel, estilo.panel(), estilo.valor());
        escribir(
                g,
                Disposicion.MOVIMIENTOS,
                Texto.ceros(movimientos, 3),
                estilo.panel(),
                estilo.valor());
        escribir(g, Disposicion.EMPUJES, Texto.ceros(empujes, 3), estilo.panel(), estilo.valor());
        dibujarUndo(g, estilo);
    }

    /** Tapa el número de ejemplo del boceto y escribe el valor real. */
    private static void escribir(
            Graphics2D g, Rectangle zona, String valor, Color fondo, Color color) {
        g.setColor(fondo);
        g.fillRect(zona.x, zona.y, zona.width, zona.height);
        Texto.izquierda(g, valor, zona.x + 1, zona.y + zona.height - 2, Texto.VALOR, color);
    }

    /** Cuadraditos llenos = usos de Undo seguidos que quedan. */
    private void dibujarUndo(Graphics2D g, EstiloRegion estilo) {
        int quedan = Math.max(0, USOS_UNDO_SEGUIDOS - usosUndoSeguidos);
        for (int i = 0; i < Disposicion.UNDO_X.length; i++) {
            int x = Disposicion.UNDO_X[i];
            g.setColor(estilo.panel());
            g.fillRect(x, Disposicion.UNDO_Y, Disposicion.UNDO_ANCHO, Disposicion.UNDO_ALTO);
            g.setColor(estilo.undo());
            if (i < quedan) {
                g.fillRect(x, Disposicion.UNDO_Y, Disposicion.UNDO_ANCHO, Disposicion.UNDO_ALTO);
            } else {
                g.drawRect(
                        x,
                        Disposicion.UNDO_Y,
                        Disposicion.UNDO_ANCHO - 1,
                        Disposicion.UNDO_ALTO - 1);
            }
        }
    }
}
