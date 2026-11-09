package temuexpress.modelo.nucleo;

import java.util.Objects;

public final class Celda {
    private final Terreno terreno;
    private Caja caja;

    public Celda(Terreno terreno) {
        this.terreno = Objects.requireNonNull(terreno);
    }

    public Terreno getTerreno() {
        return terreno;
    }

    public Caja getCaja() {
        return caja;
    }

    public boolean tieneCaja() {
        return caja != null;
    }

    public boolean estaLibre() {
        return !tieneCaja() && terreno.esTransitablePorCaja();
    }

    /** Coloca una caja sin sobrescribir otra ni permitir objetos sobre paredes. */
    public void setCaja(Caja caja) {
        Objects.requireNonNull(caja);
        if (!estaLibre()) {
            throw new IllegalStateException("La celda no puede recibir una caja");
        }
        this.caja = caja;
    }

    Caja retirarCaja() {
        Caja anterior = caja;
        caja = null;
        return anterior;
    }
}
