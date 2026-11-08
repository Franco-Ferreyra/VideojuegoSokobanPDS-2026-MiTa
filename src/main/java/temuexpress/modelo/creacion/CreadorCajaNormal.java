package temuexpress.modelo.creacion;

import temuexpress.modelo.nucleo.Caja;
import temuexpress.modelo.nucleo.CajaNormal;

public final class CreadorCajaNormal extends CreadorCaja {
    public Caja crearCaja() {
        return new CajaNormal();
    }
}
