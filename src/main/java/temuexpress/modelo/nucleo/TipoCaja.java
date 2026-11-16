package temuexpress.modelo.nucleo;

public enum TipoCaja {
    NORMAL('$'),
    FRAGIL('F'),
    LLAVE('K');

    private final char simbolo;

    TipoCaja(char simbolo) {
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }
}
