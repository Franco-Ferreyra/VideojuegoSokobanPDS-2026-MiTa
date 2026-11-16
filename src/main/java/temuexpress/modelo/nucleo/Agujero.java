package temuexpress.modelo.nucleo;

/**
 * Agujero de Alemania. La caja normal o frágil que cae lo tapa y desaparece; tapado funciona como
 * piso. Si cae la caja llave, el nivel ya no se puede ganar.
 */
public final class Agujero implements Terreno {
    private boolean tapado;

    public void tapar() {
        tapado = true;
    }

    public boolean estaTapado() {
        return tapado;
    }

    /** Para que el Memento lo deje como estaba. */
    public void restaurarTapado(boolean tapado) {
        this.tapado = tapado;
    }

    /** El jugador no pisa agujeros abiertos. */
    public boolean esTransitablePorJugador() {
        return tapado;
    }

    /** La caja siempre puede entrar: si está abierto, cae. */
    @Override
    public boolean esTransitablePorCaja() {
        return true;
    }

    /** Tapado se ve como piso. */
    public char getSimbolo() {
        return tapado ? ' ' : 'O';
    }

    @Override
    public Terreno copiar() {
        Agujero copia = new Agujero();
        copia.tapado = tapado;
        return copia;
    }
}
