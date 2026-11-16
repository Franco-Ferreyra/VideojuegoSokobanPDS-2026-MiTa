package temuexpress.modelo.estrategias;

import java.util.Objects;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Posicion;
import temuexpress.modelo.nucleo.Tablero;

/**
 * Cinta: la caja sigue en la dirección de la cinta y aplica el efecto de la casilla nueva. Si la
 * salida está bloqueada, se queda sobre la cinta.
 */
public final class EstrategiaCinta implements EstrategiaDesplazamientoCaja {
    private final Direccion direccionCinta;

    public EstrategiaCinta(Direccion direccionCinta) {
        this.direccionCinta = Objects.requireNonNull(direccionCinta);
    }

    public Posicion calcularDestino(Tablero tablero, Posicion origen, Direccion dir) {
        Posicion cinta = origen.siguiente(dir);
        if (!Encadenamiento.libre(tablero, cinta)) return null;
        if (!Encadenamiento.libre(tablero, cinta.siguiente(direccionCinta))) return cinta;
        return Encadenamiento.continuar(tablero, cinta, direccionCinta);
    }
}
