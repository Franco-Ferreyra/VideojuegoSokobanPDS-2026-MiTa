package temuexpress.modelo.dto;

/**
 * Evento que Partida envía a sus observadores (diagrama 8). Inmutable.
 *
 * <p>tipo: MOVIMIENTO, UNDO, REINICIO, VICTORIA o DERROTA. nivel: formato región-subnivel, por
 * ejemplo "1-1".
 */
public record EventoPartidaDTO(
        String tipo, String nivel, int movimientos, int empujes, int usosUndoSeguidos) {
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
