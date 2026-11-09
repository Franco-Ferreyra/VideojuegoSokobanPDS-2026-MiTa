package temuexpress.modelo.estrategias;

import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Posicion;
import temuexpress.modelo.nucleo.Tablero;

public interface EstrategiaDesplazamientoCaja {
    /** Devuelve null si no se puede completar el desplazamiento; no modifica el tablero. */
    Posicion calcularDestino(Tablero tablero, Posicion origen, Direccion dir);
}
