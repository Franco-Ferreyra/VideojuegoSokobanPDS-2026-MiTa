package temuexpress.modelo.nucleo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import temuexpress.modelo.dto.EventoPartidaDTO;
import temuexpress.modelo.dto.TableroDTO;
import temuexpress.modelo.observador.ObservadorPartida;
import temuexpress.modelo.observador.PublicadorPartida;

/**
 * Originator del Memento, contexto del Strategy y publicador del Observer. Avisa cada cambio con un
 * EventoPartidaDTO (ver tipos en EventoPartidaDTO).
 */
public final class Partida implements PublicadorPartida {
    private static final int MAX_USOS_UNDO_SEGUIDOS = 3;
    private final String numero;
    private final List<ObservadorPartida> observadores = new ArrayList<>();
    private final Tablero tablero;
    private final Object propietario = new Object();
    private final MementoPartida mementoInicial;
    private int movimientos, empujes, usosUndo, usosUndoSeguidos;

    public Partida(Nivel nivel) {
        numero = Objects.requireNonNull(nivel).getNumero();
        tablero = nivel.getTablero();
        actualizarMuros();
        mementoInicial = crearMemento();
    }

    public boolean mover(Direccion dir) {
        Objects.requireNonNull(dir);
        if (esVictoria() || esDerrota()) return false;
        Posicion siguiente = tablero.getPosicionJugador().siguiente(dir);
        if (!tablero.contiene(siguiente)) return false;
        Celda celda = tablero.getCelda(siguiente);
        boolean empujo = celda.tieneCaja();
        // Caminando no pisa cintas; pero si empuja una caja trabada sobre una cinta, queda encima
        // (misma regla con la que se verificaron los mapas).
        if (!empujo && !celda.getTerreno().esTransitablePorJugador()) return false;
        boolean abiertosAntes = cerrojosCompletos();
        List<String> tipos = new ArrayList<>();
        if (empujo && !empujar(siguiente, dir, tipos)) return false;
        if (!empujo) tipos.add("MOVIMIENTO");
        tablero.setPosicionJugador(siguiente);
        actualizarMuros();
        movimientos++;
        if (empujo) empujes++;
        usosUndoSeguidos = 0;
        boolean abiertosDespues = cerrojosCompletos();
        if (abiertosDespues != abiertosAntes)
            tipos.add(abiertosDespues ? "MURO_ABRE" : "MURO_CIERRA");
        if (esVictoria()) tipos.add("VICTORIA");
        if (esDerrota()) tipos.add("DERROTA");
        for (String tipo : tipos) notificar(evento(tipo));
        return true;
    }

    private EventoPartidaDTO evento(String tipo) {
        return new EventoPartidaDTO(tipo, numero, movimientos, empujes, usosUndoSeguidos);
    }

    public void suscribir(ObservadorPartida o) {
        observadores.add(Objects.requireNonNull(o));
    }

    public void desuscribir(ObservadorPartida o) {
        observadores.remove(o);
    }

    public void notificar(EventoPartidaDTO evento) {
        for (ObservadorPartida o : List.copyOf(observadores)) o.actualizar(evento);
    }

    /** Tipo de empuje según el terreno al que entra la caja (para el sonido). */
    private static String tipoEmpuje(Terreno entrada) {
        if (entrada instanceof Hielo) return "RESBALAR";
        if (entrada instanceof Cinta) return "CINTA";
        if (entrada instanceof Portal) return "PORTAL";
        return "EMPUJE";
    }

    /**
     * Empuja la caja de origen. Los muros se calculan sin contar la caja empujada (si la llave sale
     * del cerrojo, los muros ya están cerrados para ella). Si no se puede, deja todo como estaba.
     */
    private boolean empujar(Posicion origen, Direccion dir, List<String> tipos) {
        Posicion entrada = origen.siguiente(dir);
        if (!tablero.contiene(entrada)) return false;
        Celda celdaOrigen = tablero.getCelda(origen);
        Caja caja = celdaOrigen.retirarCaja();
        actualizarMuros(origen);
        Posicion destino =
                tablero.getCelda(entrada)
                        .getTerreno()
                        .getEstrategia()
                        .calcularDestino(tablero, origen, dir);
        if (destino == null) {
            celdaOrigen.setCaja(caja);
            actualizarMuros();
            return false;
        }
        tipos.add(tipoEmpuje(tablero.getCelda(entrada).getTerreno()));
        caja.alSerEmpujada();
        if (caja.getTipo() == TipoCaja.FRAGIL)
            tipos.add(caja.estaRota() ? "ROTURA" : "FRAGIL_DANO");
        Celda celdaDestino = tablero.getCelda(destino);
        // Si cae en un agujero abierto, lo tapa y desaparece. Una caja rota queda a la vista.
        if (!caja.estaRota()
                && celdaDestino.getTerreno() instanceof Agujero agujero
                && !agujero.estaTapado()) {
            agujero.tapar();
            tipos.add("CAIDA");
        } else {
            celdaDestino.setCaja(caja);
            if (caja.estaEntregada(celdaDestino)) tipos.add("META");
        }
        return true;
    }

    private List<Posicion> todasLasPosiciones() {
        List<Posicion> posiciones = new ArrayList<>();
        for (int f = 0; f < tablero.getAlto(); f++)
            for (int c = 0; c < tablero.getAncho(); c++) posiciones.add(new Posicion(f, c));
        return posiciones;
    }

    /**
     * Abre los muros si todos los cerrojos tienen una caja llave; si no, los cierra. Un muro con el
     * jugador, una caja o una de las posiciones ocupadas encima queda abierto hasta que se libera.
     */
    private void actualizarMuros(Posicion... ocupadas) {
        boolean abiertos = cerrojosCompletos();
        List<Posicion> extra = List.of(ocupadas);
        for (int f = 0; f < tablero.getAlto(); f++) {
            for (int c = 0; c < tablero.getAncho(); c++) {
                Posicion p = new Posicion(f, c);
                Celda celda = tablero.getCelda(p);
                if (!(celda.getTerreno() instanceof Muro muro)) continue;
                boolean ocupado =
                        celda.tieneCaja()
                                || p.equals(tablero.getPosicionJugador())
                                || extra.contains(p);
                if (abiertos || ocupado) muro.abrir();
                else muro.cerrar();
            }
        }
    }

    private boolean cerrojosCompletos() {
        for (int f = 0; f < tablero.getAlto(); f++) {
            for (int c = 0; c < tablero.getAncho(); c++) {
                Celda celda = tablero.getCelda(new Posicion(f, c));
                if (celda.getTerreno().esCerrojo()
                        && !(celda.tieneCaja() && celda.getCaja().getTipo() == TipoCaja.LLAVE)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void abrirTodosLosMuros() {
        for (int f = 0; f < tablero.getAlto(); f++) {
            for (int c = 0; c < tablero.getAncho(); c++) {
                if (tablero.getCelda(new Posicion(f, c)).getTerreno() instanceof Muro muro) {
                    muro.abrir();
                }
            }
        }
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

    /** Se pierde si se rompió una caja frágil o si una caja llave cayó en un agujero. */
    public boolean esDerrota() {
        boolean hayRota =
                tablero.getPosicionesCajas().stream()
                        .anyMatch(p -> tablero.getCelda(p).getCaja().estaRota());
        return hayRota || cantidadLlaves() < cantidadCerrojos();
    }

    private long cantidadLlaves() {
        return tablero.getPosicionesCajas().stream()
                .filter(p -> tablero.getCelda(p).getCaja().getTipo() == TipoCaja.LLAVE)
                .count();
    }

    private long cantidadCerrojos() {
        return todasLasPosiciones().stream()
                .filter(p -> tablero.getCelda(p).getTerreno().esCerrojo())
                .count();
    }

    public MementoPartida crearMemento() {
        List<EstadoCaja> cajas =
                tablero.getPosicionesCajas().stream()
                        .map(p -> EstadoCaja.capturar(tablero.getCelda(p).getCaja(), p))
                        .toList();
        List<Posicion> agujerosTapados =
                todasLasPosiciones().stream()
                        .filter(
                                p ->
                                        tablero.getCelda(p).getTerreno() instanceof Agujero a
                                                && a.estaTapado())
                        .toList();
        return new MementoPartida(
                propietario, tablero.getPosicionJugador(), cajas, agujerosTapados);
    }

    public void restaurar(MementoPartida memento) {
        Objects.requireNonNull(memento);
        if (memento.getPropietario() != propietario)
            throw new IllegalArgumentException("Memento de otra partida");
        for (Posicion p : tablero.getPosicionesCajas()) tablero.getCelda(p).retirarCaja();
        for (Posicion p : todasLasPosiciones()) {
            if (tablero.getCelda(p).getTerreno() instanceof Agujero agujero) {
                agujero.restaurarTapado(memento.getAgujerosTapados().contains(p));
            }
        }
        // Se abren para poder volver a poner cajas y jugador; después se recalculan.
        abrirTodosLosMuros();
        for (EstadoCaja caja : memento.getCajas())
            tablero.getCelda(caja.posicion()).setCaja(caja.reconstruir());
        tablero.setPosicionJugador(memento.getPosicionJugador());
        actualizarMuros();
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
        notificar(evento("UNDO"));
    }

    public void reiniciar() {
        restaurar(mementoInicial);
        movimientos = 0;
        empujes = 0;
        usosUndo = 0;
        usosUndoSeguidos = 0;
        notificar(evento("REINICIO"));
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
        if (!celda.tieneCaja()) return ' ';
        Caja caja = celda.getCaja();
        // La frágil viaja con la resistencia que le queda (4 a 1, 0 = rota) para que se vea el
        // daño.
        if (caja instanceof CajaFragil fragil) {
            return Character.forDigit(fragil.getResistencia(), 10);
        }
        return caja.getTipo().getSimbolo();
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
