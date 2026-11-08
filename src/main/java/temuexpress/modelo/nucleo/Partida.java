package temuexpress.modelo.nucleo;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import temuexpress.modelo.dto.TableroDTO;

public final class Partida {
    private static final int MAX_USOS_UNDO_SEGUIDOS = 3;
    private final Tablero tablero;
    private final Object propietario = new Object();
    private final MementoPartida mementoInicial;
    private int movimientos, empujes, usosUndo, usosUndoSeguidos;

    public Partida(Nivel nivel) {
        tablero = Objects.requireNonNull(nivel).getTablero();
        mementoInicial = crearMemento();
    }

    public boolean mover(Direccion dir) {
        Objects.requireNonNull(dir);
        if (esVictoria() || esDerrota()) return false;
        Posicion siguiente = tablero.getPosicionJugador().siguiente(dir);
        if (!tablero.contiene(siguiente)) return false;
        Celda celda = tablero.getCelda(siguiente);
        if (!celda.getTerreno().esTransitablePorJugador()) return false;
        boolean empujo = celda.tieneCaja();
        if (empujo) {
            Posicion entrada = siguiente.siguiente(dir);
            if (!tablero.contiene(entrada)) return false;
            Posicion destino =
                    tablero.getCelda(entrada)
                            .getTerreno()
                            .getEstrategia()
                            .calcularDestino(tablero, siguiente, dir);
            if (destino == null) return false;
            Caja caja = celda.retirarCaja();
            caja.alSerEmpujada();
            tablero.getCelda(destino).setCaja(caja);
        }
        tablero.setPosicionJugador(siguiente);
        movimientos++;
        if (empujo) empujes++;
        usosUndoSeguidos = 0;
        return true;
    }

    public boolean esVictoria() {
        List<Posicion> cajas = tablero.getPosicionesCajas();
        if (cajas.isEmpty()) {
            return false;
        }
        for (Posicion posicion : cajas) {
            Celda celda = tablero.getCelda(posicion);
            if (!celda.getCaja().estaEntregada(celda)) {
                return false;
            }
        }
        return true;
    }

    public boolean esDerrota() {
        return tablero.getPosicionesCajas().stream()
                .anyMatch(p -> tablero.getCelda(p).getCaja().estaRota());
    }

    public MementoPartida crearMemento() {
        List<EstadoCaja> cajas =
                tablero.getPosicionesCajas().stream()
                        .map(p -> EstadoCaja.capturar(tablero.getCelda(p).getCaja(), p))
                        .toList();
        return new MementoPartida(propietario, tablero.getPosicionJugador(), cajas, List.of());
    }

    public void restaurar(MementoPartida memento) {
        Objects.requireNonNull(memento);
        if (memento.getPropietario() != propietario)
            throw new IllegalArgumentException("Memento de otra partida");
        for (Posicion p : tablero.getPosicionesCajas()) tablero.getCelda(p).retirarCaja();
        for (EstadoCaja caja : memento.getCajas())
            tablero.getCelda(caja.posicion()).setCaja(caja.reconstruir());
        tablero.setPosicionJugador(memento.getPosicionJugador());
    }

    public boolean puedeUsarUndo() {
        return usosUndoSeguidos < MAX_USOS_UNDO_SEGUIDOS;
    }

    public int getUsosUndoRestantes() {
        return MAX_USOS_UNDO_SEGUIDOS - usosUndoSeguidos;
    }

    public void registrarUsoUndo() {
        if (!puedeUsarUndo())
            throw new IllegalStateException("No quedan usos consecutivos de Undo");
        usosUndo++;
        usosUndoSeguidos++;
    }

    public void reiniciar() {
        restaurar(mementoInicial);
        movimientos = 0;
        empujes = 0;
        usosUndo = 0;
        usosUndoSeguidos = 0;
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

    public int getUsosUndoSeguidos() {
        return usosUndoSeguidos;
    }

    private char simboloObjeto(Posicion posicion, Celda celda) {
        if (posicion.equals(tablero.getPosicionJugador())) return '@';
        return celda.tieneCaja() ? celda.getCaja().getTipo().getSimbolo() : ' ';
    }

    public TableroDTO getTableroDTO() {
        StringJoiner terreno = new StringJoiner("\n"), objetos = new StringJoiner("\n");
        for (int f = 0; f < tablero.getAlto(); f++) {
            StringBuilder t = new StringBuilder(), o = new StringBuilder();
            for (int c = 0; c < tablero.getAncho(); c++) {
                Posicion p = new Posicion(f, c);
                Celda celda = tablero.getCelda(p);
                t.append(celda.getTerreno().getSimbolo());
                o.append(simboloObjeto(p, celda));
            }
            terreno.add(t);
            objetos.add(o);
        }
        return new TableroDTO(
                tablero.getAncho(), tablero.getAlto(), terreno.toString(), objetos.toString());
    }
}
