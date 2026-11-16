package temuexpress.modelo.nucleo;

import java.util.Objects;
import temuexpress.modelo.estrategias.EstrategiaDesplazamientoCaja;
import temuexpress.modelo.estrategias.EstrategiaPortal;

/**
 * Portal de Perú - Venezuela. Van de a pares con el mismo número; la caja sale por la pareja en la
 * dirección en que venía. Solo pasan cajas.
 */
public final class Portal implements Terreno {
    private final int idPar;
    private Posicion pareja;

    public Portal(int idPar) {
        if (idPar < 1 || idPar > 9) {
            throw new IllegalArgumentException("Portal fuera de rango: " + idPar);
        }
        this.idPar = idPar;
    }

    public int getIdPar() {
        return idPar;
    }

    /** Posición del otro portal del mismo número (por ahí sale la caja). */
    public Posicion getPareja() {
        return pareja;
    }

    /** Lo usa el Builder al vincular los dos portales del mismo número. */
    public void setPareja(Posicion pareja) {
        this.pareja = Objects.requireNonNull(pareja);
    }

    public boolean esTransitablePorJugador() {
        return false;
    }

    @Override
    public boolean esTransitablePorCaja() {
        return true;
    }

    public char getSimbolo() {
        return (char) ('0' + idPar);
    }

    @Override
    public EstrategiaDesplazamientoCaja getEstrategia() {
        return new EstrategiaPortal(this);
    }
}
