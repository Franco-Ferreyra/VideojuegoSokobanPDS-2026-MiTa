package temuexpress.modelo.creacion;

import temuexpress.modelo.nucleo.Caja;
import temuexpress.modelo.nucleo.CajaLlave;

public final class CreadorCajaLlave extends CreadorCaja {
    public Caja crearCaja() {
        return new CajaLlave();
    }
}
