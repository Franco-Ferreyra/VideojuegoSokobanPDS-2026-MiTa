package temuexpress.modelo.nucleo;

import java.util.Objects;

/** Estado inmutable de una caja; centraliza su captura y reconstrucciÃ³n. */
record EstadoCaja(TipoCaja tipo, Posicion posicion, int resistencia) {
    EstadoCaja {
        Objects.requireNonNull(tipo);
        Objects.requireNonNull(posicion);
    }

    static EstadoCaja capturar(Caja caja, Posicion posicion) {
        return switch (caja.getTipo()) {
            case NORMAL -> new EstadoCaja(TipoCaja.NORMAL, posicion, 0);
        };
    }

    Caja reconstruir() {
        return switch (tipo) {
            case NORMAL -> new CajaNormal();
        };
    }
}
