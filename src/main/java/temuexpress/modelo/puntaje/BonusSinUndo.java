package temuexpress.modelo.puntaje;

import temuexpress.modelo.dto.ResumenNivelDTO;

/** +200 si no se usó Undo. */
public final class BonusSinUndo extends DecoradorPuntaje {
    public BonusSinUndo(CalculadorPuntaje envuelto) {
        super(envuelto);
    }

    @Override
    public int calcular(ResumenNivelDTO resumen) {
        return super.calcular(resumen) + (resumen.getUsosUndo() == 0 ? 200 : 0);
    }
}
