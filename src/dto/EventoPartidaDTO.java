package dto;

/**
 * Evento que Partida envía a sus observadores (diagrama 8). Inmutable.
 *
 * tipo: MOVIMIENTO, UNDO, REINICIO, VICTORIA o DERROTA.
 * nivel: formato región-subnivel, por ejemplo "1-1".
 */
public class EventoPartidaDTO {

    private final String tipo;
    private final String nivel;
    private final int movimientos;
    private final int empujes;
    private final int usosUndoSeguidos;

    public EventoPartidaDTO(String tipo, String nivel, int movimientos, int empujes, int usosUndoSeguidos) {
        this.tipo = tipo;
        this.nivel = nivel;
        this.movimientos = movimientos;
        this.empujes = empujes;
        this.usosUndoSeguidos = usosUndoSeguidos;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNivel() {
        return nivel;
    }

    public int getMovimientos() {
        return movimientos;
    }

    public int getEmpujes() {
        return empujes;
    }

    public int getUsosUndoSeguidos() {
        return usosUndoSeguidos;
    }
}
