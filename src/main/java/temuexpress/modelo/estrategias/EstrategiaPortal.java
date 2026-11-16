package temuexpress.modelo.estrategias;

import java.util.Objects;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Portal;
import temuexpress.modelo.nucleo.Posicion;
import temuexpress.modelo.nucleo.Tablero;

/**
 * Portal: la caja aparece en la pareja y sale una casilla en la dirección en que venía. Si esa
 * salida está bloqueada, el empuje no se hace.
 */
public final class EstrategiaPortal implements EstrategiaDesplazamientoCaja {
    private final Posicion salida;

    public EstrategiaPortal(Portal portal) {
        this.salida = Objects.requireNonNull(Objects.requireNonNull(portal).getPareja());
    }

    public Posicion calcularDestino(Tablero tablero, Posicion origen, Direccion dir) {
        Posicion entrada = origen.siguiente(dir);
        if (!Encadenamiento.libre(tablero, entrada)) return null;
        if (!Encadenamiento.libre(tablero, salida.siguiente(dir))) return null;
        return Encadenamiento.continuar(tablero, salida, dir);
    }
}
