package temuexpress.controlador;

import java.util.Objects;
import temuexpress.modelo.dto.TableroDTO;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.HistorialPartida;
import temuexpress.modelo.nucleo.MementoPartida;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.modelo.nucleo.Partida;

/** Coordina las acciones de usuario sin depender de Swing. */
public final class ControladorJuego {
    private Partida partida;
    private final HistorialPartida historial = new HistorialPartida();

    public ControladorJuego(Nivel nivel) {
        iniciarNivel(nivel);
    }

    public void iniciarNivel(Nivel nivel) {
        partida = new Partida(Objects.requireNonNull(nivel));
        historial.limpiar();
    }

    public boolean mover(Direccion dir) {
        MementoPartida anterior = partida.crearMemento();
        if (!partida.mover(dir)) return false;
        historial.guardar(anterior);
        return true;
    }

    public boolean deshacer() {
        if (!partida.puedeUsarUndo()) return false;
        MementoPartida anterior = historial.deshacer();
        if (anterior == null) return false;
        partida.restaurar(anterior);
        partida.registrarUsoUndo();
        return true;
    }

    public void reiniciarNivel() {
        partida.reiniciar();
        historial.limpiar();
    }

    public TableroDTO getTableroDTO() {
        return partida.getTableroDTO();
    }

    public boolean esVictoria() {
        return partida.esVictoria();
    }

    public int getMovimientos() {
        return partida.getMovimientos();
    }

    public int getEmpujes() {
        return partida.getEmpujes();
    }

    public int getUsosUndo() {
        return partida.getUsosUndo();
    }

    public int getUsosUndoRestantes() {
        return partida.getUsosUndoRestantes();
    }

    public int getUsosUndoSeguidos() {
        return partida.getUsosUndoSeguidos();
    }
}
