package temuexpress.modelo.dto;

/**
 * Evento que Partida envía a sus observadores (diagrama 8). Inmutable.
 *
 * <p>tipo: MOVIMIENTO (paso sin empujar), EMPUJE, RESBALAR, CINTA o PORTAL (empuje según el terreno
 * al que entra la caja), CAIDA, FRAGIL_DANO, ROTURA, META, MURO_ABRE, MURO_CIERRA, VICTORIA,
 * DERROTA, UNDO o REINICIO. Un movimiento puede generar varios eventos seguidos. nivel: formato
 * región-subnivel, por ejemplo "1-1".
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
