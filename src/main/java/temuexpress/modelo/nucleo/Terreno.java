package temuexpress.modelo.nucleo;

import temuexpress.modelo.estrategias.EstrategiaDesplazamientoCaja;
import temuexpress.modelo.estrategias.EstrategiaNormal;

public interface Terreno {
    boolean esTransitablePorJugador();

    default boolean esTransitablePorCaja() {
        return esTransitablePorJugador();
    }

    default boolean esDestino() {
        return false;
    }

    default boolean esCerrojo() {
        return false;
    }

    char getSimbolo();

    default EstrategiaDesplazamientoCaja getEstrategia() {
        return new EstrategiaNormal();
    }
}
