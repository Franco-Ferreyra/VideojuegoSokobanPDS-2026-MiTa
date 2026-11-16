package temuexpress.modelo.puntaje;

import temuexpress.modelo.dto.ResumenNivelDTO;

/** +150 si se terminó en el PAR del nivel o menos movimientos. */
public final class BonusPorPar extends DecoradorPuntaje {
    public BonusPorPar(CalculadorPuntaje envuelto) {
        super(envuelto);
    }

    @Override
    public int calcular(ResumenNivelDTO resumen) {
        return super.calcular(resumen) + (resumen.getMovimientos() <= resumen.getPar() ? 150 : 0);
    }
}
