package temuexpress.vista;

import temuexpress.modelo.dto.EventoPartidaDTO;
import temuexpress.modelo.dto.TableroDTO;

/**
 * Lo que necesita el presentador de la partida (Observer) para mostrar los cambios. Lo implementa
 * VentanaJuego; así el presentador depende de una interfaz chica y no de toda la ventana.
 */
public interface VistaPartida {
    void mostrarJuego(TableroDTO tablero);

    void actualizar(TableroDTO tablero, EventoPartidaDTO evento);

    void mostrarDerrota(String[] lineas);

    /** Frase corta del repartidor sobre el juego (por ejemplo, al entregar una caja). */
    void mostrarDialogo(String frase);
}
