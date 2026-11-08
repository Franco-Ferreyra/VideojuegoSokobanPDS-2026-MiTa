package temuexpress.modelo.nucleo;

public final class CajaNormal implements Caja {
    public boolean estaEntregada(Celda celda) {
        return celda.getTerreno().esDestino();
    }

    public void alSerEmpujada() {}

    public boolean estaRota() {
        return false;
    }

    public TipoCaja getTipo() {
        return TipoCaja.NORMAL;
    }
}
