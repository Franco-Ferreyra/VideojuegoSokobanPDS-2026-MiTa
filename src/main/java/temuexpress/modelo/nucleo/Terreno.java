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

    /**
     * Copia para otro tablero. Los terrenos sin estado se comparten; los que tienen estado (muro,
     * agujero) devuelven uno nuevo para que dos partidas no se pisen.
     */
    default Terreno copiar() {
        return this;
    }
}
