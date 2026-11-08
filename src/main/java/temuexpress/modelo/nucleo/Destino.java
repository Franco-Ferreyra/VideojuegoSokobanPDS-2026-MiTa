package temuexpress.modelo.nucleo;

public final class Destino implements Terreno {
    public boolean esTransitablePorJugador() {
        return true;
    }

    public boolean esDestino() {
        return true;
    }

    public char getSimbolo() {
        return '.';
    }
}
