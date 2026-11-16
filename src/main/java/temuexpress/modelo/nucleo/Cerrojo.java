package temuexpress.modelo.nucleo;

/**
 * Casilla donde va la caja llave. Cuando todos los cerrojos tienen su llave, se abren los muros.
 */
public final class Cerrojo implements Terreno {
    public boolean esTransitablePorJugador() {
        return true;
    }

    public boolean esCerrojo() {
        return true;
    }

    public char getSimbolo() {
        return 'L';
    }
}
