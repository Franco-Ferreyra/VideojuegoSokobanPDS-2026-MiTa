package temuexpress.modelo.nucleo;

public final class Pared implements Terreno {
    public boolean esTransitablePorJugador() {
        return false;
    }

    public boolean esDestino() {
        return false;
    }

    public char getSimbolo() {
        return '#';
    }
}
