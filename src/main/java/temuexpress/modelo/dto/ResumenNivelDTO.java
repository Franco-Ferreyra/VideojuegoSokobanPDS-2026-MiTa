package temuexpress.modelo.dto;

/**
 * Datos del nivel terminado (diagramas 0 y 9). Inmutable. Lo usan CalculadorPuntaje y la pantalla
 * de resumen.
 */
public record ResumenNivelDTO(int movimientos, int empujes, int usosUndo, int par) {
    public int getMovimientos() {
        return movimientos;
    }

    public int getEmpujes() {
        return empujes;
    }

    public int getUsosUndo() {
        return usosUndo;
    }

    public int getPar() {
        return par;
    }
}
