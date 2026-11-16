package temuexpress.modelo.puntaje;

import java.util.Objects;
import temuexpress.modelo.dto.ResumenNivelDTO;

/** Decorador: envuelve a otro calculador y le suma o resta su parte. */
public abstract class DecoradorPuntaje implements CalculadorPuntaje {
    private final CalculadorPuntaje envuelto;

    protected DecoradorPuntaje(CalculadorPuntaje envuelto) {
        this.envuelto = Objects.requireNonNull(envuelto);
    }

    public int calcular(ResumenNivelDTO resumen) {
        return envuelto.calcular(resumen);
    }
}
