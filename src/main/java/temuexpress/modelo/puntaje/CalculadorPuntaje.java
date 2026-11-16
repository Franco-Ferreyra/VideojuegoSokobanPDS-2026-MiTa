package temuexpress.modelo.puntaje;

import temuexpress.modelo.dto.ResumenNivelDTO;

/** Componente del Decorator de puntaje (sistema de bonificaciones TeMU). */
public interface CalculadorPuntaje {
    int calcular(ResumenNivelDTO resumen);
}
