package temuexpress.modelo.nucleo;

import temuexpress.modelo.estrategias.EstrategiaDesplazamientoCaja;
import temuexpress.modelo.estrategias.EstrategiaResbaladiza;

/** Hielo de Argentina - Chile: la caja patina; el jugador camina sin resbalar. */
public final class Hielo implements Terreno {
    public boolean esTransitablePorJugador() {
        return true;
    }

    public char getSimbolo() {
        return '~';
    }

    @Override
    public EstrategiaDesplazamientoCaja getEstrategia() {
        return new EstrategiaResbaladiza();
    }
}
