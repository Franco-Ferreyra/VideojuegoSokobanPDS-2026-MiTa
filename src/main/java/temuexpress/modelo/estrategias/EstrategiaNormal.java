package temuexpress.modelo.estrategias;

import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Posicion;
import temuexpress.modelo.nucleo.Tablero;

public final class EstrategiaNormal implements EstrategiaDesplazamientoCaja {
    public Posicion calcularDestino(Tablero tablero, Posicion origen, Direccion dir) {
        Posicion destino = origen.siguiente(dir);
        return tablero.contiene(destino) && tablero.getCelda(destino).estaLibre() ? destino : null;
    }
}
