package temuexpress.modelo.nucleo;

import java.util.Objects;

/** Estado inmutable de una caja; centraliza su captura y reconstrucción. */
record EstadoCaja(TipoCaja tipo, Posicion posicion, int resistencia) {
    EstadoCaja {
        Objects.requireNonNull(tipo);
        Objects.requireNonNull(posicion);
    }

    static EstadoCaja capturar(Caja caja, Posicion posicion) {
        int resistencia = caja instanceof CajaFragil fragil ? fragil.getResistencia() : 0;
        return new EstadoCaja(caja.getTipo(), posicion, resistencia);
    }

    Caja reconstruir() {
        return switch (tipo) {
            case NORMAL -> new CajaNormal();
            case FRAGIL -> new CajaFragil(resistencia);
            case LLAVE -> new CajaLlave();
        };
    }
}
