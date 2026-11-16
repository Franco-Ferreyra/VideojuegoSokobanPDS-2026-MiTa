package temuexpress.modelo.observador;

import temuexpress.modelo.dto.EventoPartidaDTO;

/** Observer: lo implementa Partida. */
public interface PublicadorPartida {
    void suscribir(ObservadorPartida o);

    void desuscribir(ObservadorPartida o);

    void notificar(EventoPartidaDTO evento);
}
