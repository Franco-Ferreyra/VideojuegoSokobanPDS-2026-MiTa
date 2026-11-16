package temuexpress.modelo.nucleo;

/**
 * Caja que pierde 1 de resistencia por empuje. Empieza en 4: aguanta 3 empujes y el 4º la rompe.
 */
public final class CajaFragil implements Caja {
    public static final int RESISTENCIA_INICIAL = 4;
    private int resistencia;

    public CajaFragil() {
        this(RESISTENCIA_INICIAL);
    }

    /** Para que el Memento la reconstruya con la resistencia que tenía. */
    CajaFragil(int resistencia) {
        if (resistencia < 0 || resistencia > RESISTENCIA_INICIAL) {
            throw new IllegalArgumentException("Resistencia fuera de rango: " + resistencia);
        }
        this.resistencia = resistencia;
    }

    public boolean estaEntregada(Celda celda) {
        return !estaRota() && celda.getTerreno().esDestino();
    }

    public void alSerEmpujada() {
        if (resistencia > 0) resistencia--;
    }

    public boolean estaRota() {
        return resistencia == 0;
    }

    public int getResistencia() {
        return resistencia;
    }

    public TipoCaja getTipo() {
        return TipoCaja.FRAGIL;
    }
}
