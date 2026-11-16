package temuexpress.modelo.nucleo;

import java.util.Objects;
import temuexpress.modelo.estrategias.EstrategiaCinta;
import temuexpress.modelo.estrategias.EstrategiaDesplazamientoCaja;

/** Cinta de Ecuador - Colombia: lleva la caja en su dirección. El jugador no la pisa. */
public final class Cinta implements Terreno {
    private final Direccion direccion;

    public Cinta(Direccion direccion) {
        this.direccion = Objects.requireNonNull(direccion);
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public boolean esTransitablePorJugador() {
        return false;
    }

    @Override
    public boolean esTransitablePorCaja() {
        return true;
    }

    public char getSimbolo() {
        return switch (direccion) {
            case ARRIBA -> '^';
            case ABAJO -> 'v';
            case IZQUIERDA -> '<';
            case DERECHA -> '>';
        };
    }

    @Override
    public EstrategiaDesplazamientoCaja getEstrategia() {
        return new EstrategiaCinta(direccion);
    }
}
