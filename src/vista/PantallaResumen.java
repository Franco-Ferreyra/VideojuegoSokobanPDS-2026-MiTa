package vista;

import dto.ResumenNivelDTO;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/** Resumen al ganar un nivel: estadísticas y puntaje obtenido. */
class PantallaResumen implements Pantalla {

    private static final int INTERLINEA = 18;

    private final Cartel cartel = new Cartel(new Rectangle(96, 60, 240, 228), true);
    private final ResumenNivelDTO resumen;
    private final int puntaje;

    PantallaResumen(ResumenNivelDTO resumen, int puntaje) {
        this.resumen = resumen;
        this.puntaje = puntaje;
    }

    @Override
    public void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites) {
        cartel.dibujarFondo(g, estilo);
        cartel.titulo(g, "NIVEL SUPERADO", estilo);
        int y = cartel.zona().y + 62;
        fila(g, "MOVIMIENTOS", resumen.getMovimientos(), y, estilo);
        fila(g, "EMPUJES", resumen.getEmpujes(), y + INTERLINEA, estilo);
        fila(g, "USOS DE UNDO", resumen.getUsosUndo(), y + 2 * INTERLINEA, estilo);
        fila(g, "PAR DEL NIVEL", resumen.getPar(), y + 3 * INTERLINEA, estilo);
        int linea = y + 3 * INTERLINEA + 10;
        Rectangle z = cartel.zona();
        g.setColor(estilo.borde());
        g.drawLine(z.x + 16, linea, z.x + z.width - 17, linea);
        fila(g, "PUNTAJE", puntaje, linea + 22, estilo);
        cartel.pie(g, "ENTER para continuar", estilo);
    }

    private void fila(Graphics2D g, String etiqueta, int valor, int y, EstiloRegion estilo) {
        Rectangle z = cartel.zona();
        Texto.izquierda(g, etiqueta, z.x + 16, y, Texto.CUERPO, Texto.BLANCO);
        Texto.derecha(g, String.valueOf(valor), z.x + z.width - 16, y, Texto.VALOR, estilo.valor());
    }
}
