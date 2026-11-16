package temuexpress.modelo.puntaje;

import temuexpress.modelo.dto.ResumenNivelDTO;

/** Base: 1000 - 3 x movimientos - 5 x empujes - 100 x usos de Undo. */
public final class PuntajeBase implements CalculadorPuntaje {
    public int calcular(ResumenNivelDTO resumen) {
        return 1000
                - 3 * resumen.getMovimientos()
                - 5 * resumen.getEmpujes()
                - 100 * resumen.getUsosUndo();
    }
}
