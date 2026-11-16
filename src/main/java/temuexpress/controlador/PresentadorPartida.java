package temuexpress.controlador;

import java.util.Objects;
import java.util.function.Supplier;
import temuexpress.modelo.dto.EventoPartidaDTO;
import temuexpress.modelo.dto.TableroDTO;
import temuexpress.modelo.observador.ObservadorPartida;
import temuexpress.vista.VistaPartida;

/**
 * Observer: con cada evento de la partida redibuja el tablero y el HUD, muestra la derrota con la
 * frase del repartidor, y lo que dice al entregar una caja o cuando una cae a un agujero.
 */
public final class PresentadorPartida implements ObservadorPartida {
    private final VistaPartida ventana;
    private final Supplier<TableroDTO> tablero;
    private final String nivel;
    private int derrotas;
    private int entregas;
    private int caidas;

    public PresentadorPartida(VistaPartida ventana, Supplier<TableroDTO> tablero, String nivel) {
        this.ventana = Objects.requireNonNull(ventana);
        this.tablero = Objects.requireNonNull(tablero);
        this.nivel = Objects.requireNonNull(nivel);
    }

    @Override
    public void actualizar(EventoPartidaDTO evento) {
        TableroDTO actual = tablero.get();
        switch (evento.getTipo()) {
            case "DERROTA" -> {
                ventana.actualizar(actual, evento);
                ventana.mostrarDerrota(MensajesRegionales.derrota(nivel, derrotas++));
            }
            case "UNDO", "REINICIO" -> {
                // Después de una derrota se vuelve a la pantalla de juego.
                ventana.mostrarJuego(actual);
                ventana.actualizar(actual, evento);
            }
            case "META" -> {
                ventana.actualizar(actual, evento);
                ventana.mostrarDialogo(MensajesRegionales.entrega(nivel, entregas++));
            }
            case "CAIDA" -> {
                ventana.actualizar(actual, evento);
                String frase = MensajesRegionales.caida(nivel, caidas);
                if (frase != null) {
                    caidas++;
                    ventana.mostrarDialogo(frase);
                }
            }
            default -> ventana.actualizar(actual, evento);
        }
    }
}
