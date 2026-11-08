package temuexpress.modelo.nucleo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/** Caretaker: almacena snapshots sin inspeccionarlos ni conocer a Partida. */
public final class HistorialPartida {
    private static final int CAPACIDAD = 15;
    private static final int MOVIMIENTOS_POR_UNDO = 5;
    private final Deque<MementoPartida> mementos = new ArrayDeque<>();

    public void guardar(MementoPartida m) {
        mementos.addLast(Objects.requireNonNull(m));
        if (mementos.size() > CAPACIDAD) mementos.removeFirst();
    }

    public MementoPartida deshacer() {
        MementoPartida resultado = null;
        for (int i = 0; i < MOVIMIENTOS_POR_UNDO && !mementos.isEmpty(); i++)
            resultado = mementos.removeLast();
        return resultado;
    }

    public void limpiar() {
        mementos.clear();
    }

    public int getCantidad() {
        return mementos.size();
    }
}
