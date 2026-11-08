package temuexpress.modelo.nucleo;

public final class Piso implements Terreno {
    public boolean esTransitablePorJugador() {
        return true;
    }

    public boolean esDestino() {
        return false;
    }

    public char getSimbolo() {
        return ' ';
    }
}
