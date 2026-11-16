package temuexpress.modelo.estrategias;

import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Posicion;
import temuexpress.modelo.nucleo.Tablero;

/**
 * Encadenamiento de efectos: cuando una caja sale de un terreno especial, entra a la casilla
 * siguiente y aplica la estrategia de ese terreno (por ejemplo, del hielo a un agujero).
 */
final class Encadenamiento {
    private Encadenamiento() {}

    static boolean libre(Tablero tablero, Posicion posicion) {
        return tablero.contiene(posicion) && tablero.getCelda(posicion).estaLibre();
    }

    /** La caja está en desde y sigue en dir: delega en la estrategia de la casilla siguiente. */
    static Posicion continuar(Tablero tablero, Posicion desde, Direccion dir) {
        return tablero.getCelda(desde.siguiente(dir))
                .getTerreno()
                .getEstrategia()
                .calcularDestino(tablero, desde, dir);
    }
}
