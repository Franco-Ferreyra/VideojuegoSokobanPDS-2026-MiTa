package temuexpress.vista;

import java.awt.Graphics2D;

/** Partida en curso: no dibuja nada encima del tablero. */
class PantallaJuego implements Pantalla {

    @Override
    public void dibujar(Graphics2D g, EstiloRegion estilo, HojaSprites sprites) {
        // el tablero y el HUD ya están dibujados
    }

    @Override
    public boolean permiteMover() {
        return true;
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
