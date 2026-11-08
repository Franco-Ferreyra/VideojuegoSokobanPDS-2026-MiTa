package temuexpress.modelo.nucleo;

public enum TipoCaja {
    NORMAL('$');

    private final char simbolo;

    TipoCaja(char simbolo) {
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }
}
