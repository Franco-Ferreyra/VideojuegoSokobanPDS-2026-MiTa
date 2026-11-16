package temuexpress.modelo.puntaje;

import temuexpress.modelo.dto.ResumenNivelDTO;

/** -100 adicional por cada uso de Undo. Solo se agrega a la cadena en 4-1 (Argentina - Chile). */
public final class RecargoUndo extends DecoradorPuntaje {
    public RecargoUndo(CalculadorPuntaje envuelto) {
        super(envuelto);
    }

    @Override
    public int calcular(ResumenNivelDTO resumen) {
        return super.calcular(resumen) - 100 * resumen.getUsosUndo();
    }
}
