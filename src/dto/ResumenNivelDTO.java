package dto;

/**
 * Datos del nivel terminado (diagramas 0 y 9). Inmutable.
 * Lo usan CalculadorPuntaje y la pantalla de resumen.
 */
public class ResumenNivelDTO {

    private final int movimientos;
    private final int empujes;
    private final int usosUndo;
    private final int par;

    public ResumenNivelDTO(int movimientos, int empujes, int usosUndo, int par) {
        this.movimientos = movimientos;
        this.empujes = empujes;
        this.usosUndo = usosUndo;
        this.par = par;
    }

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
