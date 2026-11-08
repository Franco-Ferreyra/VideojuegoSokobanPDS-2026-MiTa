package temuexpress.modelo.creacion;

import temuexpress.modelo.nucleo.Caja;
import temuexpress.modelo.nucleo.Posicion;
import temuexpress.modelo.nucleo.Tablero;

public abstract class CreadorCaja {
    public void colocarCaja(Tablero t, Posicion p) {
        t.getCelda(p).setCaja(crearCaja());
    }

    public abstract Caja crearCaja();
}
