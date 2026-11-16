package temuexpress.modelo.nucleo;

/** Caja que se entrega sobre un cerrojo (no sobre un destino). */
public final class CajaLlave implements Caja {
    public boolean estaEntregada(Celda celda) {
        return celda.getTerreno().esCerrojo();
    }

    public void alSerEmpujada() {}

    public boolean estaRota() {
        return false;
    }

    public TipoCaja getTipo() {
        return TipoCaja.LLAVE;
    }
}
