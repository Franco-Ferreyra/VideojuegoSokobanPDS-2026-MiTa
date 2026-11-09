package temuexpress.modelo.nucleo;

import java.util.List;

/** Snapshot opaco e inmutable. Solo el originator interpreta su contenido. */
public final class MementoPartida {
    private final Object propietario;
    private final Posicion posicionJugador;
    private final List<EstadoCaja> cajas;
    private final List<Posicion> agujerosTapados;

    MementoPartida(
            Object propietario, Posicion jugador, List<EstadoCaja> cajas, List<Posicion> agujeros) {
        this.propietario = propietario;
        this.posicionJugador = jugador;
        this.cajas = List.copyOf(cajas);
        this.agujerosTapados = List.copyOf(agujeros);
    }

    Object getPropietario() {
        return propietario;
    }

    Posicion getPosicionJugador() {
        return posicionJugador;
    }

    List<EstadoCaja> getCajas() {
        return cajas;
    }

    List<Posicion> getAgujerosTapados() {
        return agujerosTapados;
    }
}
