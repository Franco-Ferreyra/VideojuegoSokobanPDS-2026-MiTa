package temuexpress.modelo.estrategias;

import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Posicion;
import temuexpress.modelo.nucleo.Tablero;

/**
 * Hielo: la caja sigue en la dirección del empuje mientras pueda entrar a la casilla siguiente, y
 * aplica el efecto de esa casilla. Si está bloqueada, se queda sobre el hielo.
 */
public final class EstrategiaResbaladiza implements EstrategiaDesplazamientoCaja {
    public Posicion calcularDestino(Tablero tablero, Posicion origen, Direccion dir) {
        Posicion hielo = origen.siguiente(dir);
        if (!Encadenamiento.libre(tablero, hielo)) return null;
        if (!Encadenamiento.libre(tablero, hielo.siguiente(dir))) return hielo;
        return Encadenamiento.continuar(tablero, hielo, dir);
    }
}
