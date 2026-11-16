package temuexpress.modelo.creacion;

import temuexpress.modelo.nucleo.Caja;
import temuexpress.modelo.nucleo.CajaFragil;

public final class CreadorCajaFragil extends CreadorCaja {
    public Caja crearCaja() {
        return new CajaFragil();
    }
}
