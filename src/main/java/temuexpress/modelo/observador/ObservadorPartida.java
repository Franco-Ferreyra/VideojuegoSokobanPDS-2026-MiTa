package temuexpress.modelo.observador;

import temuexpress.modelo.dto.EventoPartidaDTO;

/** Observer: recibe lo que pasa en la partida (presentador de la vista, reacción de audio). */
public interface ObservadorPartida {
    void actualizar(EventoPartidaDTO evento);
}
