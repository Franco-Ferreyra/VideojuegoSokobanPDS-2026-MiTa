package vista;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Derrota: el cartel va sobre la escena y deja ver el tablero, porque
 * el jugador puede volver con UNDO o REINICIAR.
 */
class PantallaDerrota implements Pantalla {

    private static final int TAM_ESQUELETO = Disposicion.TAM_CASILLA * 4;

    private final Cartel cartel = new Cartel(new Rectangle(76, 56, 280, 120), false);
    private final String[] lineas;

    PantallaDerrota(String[] lineas) {
        this.lineas = lineas.clone();
    }

    @Override
    public void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites) {
        cartel.dibujarFondo(g, estilo);
        Rectangle z = cartel.zona();
        int y = z.y + (z.height - TAM_ESQUELETO) / 2;
        g.drawImage(sprites.get("esqueleto"), z.x + 14, y, TAM_ESQUELETO, TAM_ESQUELETO, null);
        int x = z.x + 14 + TAM_ESQUELETO + 12;
        Texto.izquierda(g, "PAQUETE PERDIDO", x, z.y + 32, Texto.VALOR, Texto.ROJO);
        for (int i = 0; i < lineas.length; i++) {
            Texto.izquierda(g, lineas[i], x, z.y + 54 + i * 16, Texto.CUERPO, Texto.BLANCO);
        }
        Texto.izquierda(g, "Usá UNDO o REINICIAR", x, z.y + z.height - 14, Texto.CUERPO,
                estilo.undo());
    }

    @Override
    public boolean botonesActivos() {
        return true;
    }

    @Override
    public boolean permiteContinuar() {
        return false;
    }
}
