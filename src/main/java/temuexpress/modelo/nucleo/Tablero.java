package temuexpress.modelo.nucleo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Tablero {
    private final Celda[][] celdas;
    private Posicion jugador;

    public Tablero(Celda[][] celdas, Posicion jugador) {
        validarCeldas(celdas);
        this.celdas = new Celda[celdas.length][celdas[0].length];
        for (int fila = 0; fila < celdas.length; fila++) {
            for (int columna = 0; columna < celdas[fila].length; columna++) {
                Posicion posicion = new Posicion(fila, columna);
                Celda original = celdas[fila][columna];
                // Los tres terrenos básicos son inmutables y se pueden compartir.
                Celda copia = new Celda(original.getTerreno());
                if (original.tieneCaja()) {
                    copia.setCaja(EstadoCaja.capturar(original.getCaja(), posicion).reconstruir());
                }
                this.celdas[fila][columna] = copia;
            }
        }
        setPosicionJugador(jugador);
    }

    private static void validarCeldas(Celda[][] celdas) {
        Objects.requireNonNull(celdas);
        if (celdas.length == 0 || celdas[0] == null || celdas[0].length == 0) {
            throw new IllegalArgumentException("El tablero no puede estar vacio");
        }
        int ancho = celdas[0].length;
        for (Celda[] fila : celdas) {
            if (fila == null || fila.length != ancho) {
                throw new IllegalArgumentException("El tablero debe ser rectangular");
            }
            for (Celda celda : fila) {
                Objects.requireNonNull(celda, "El tablero contiene una celda sin terreno");
            }
        }
    }

    public int getAncho() {
        return celdas[0].length;
    }

    public int getAlto() {
        return celdas.length;
    }

    public boolean contiene(Posicion posicion) {
        return posicion.fila() >= 0
                && posicion.fila() < getAlto()
                && posicion.columna() >= 0
                && posicion.columna() < getAncho();
    }

    public Celda getCelda(Posicion posicion) {
        if (!contiene(posicion)) {
            throw new IllegalArgumentException("Posicion fuera del tablero: " + posicion);
        }
        return celdas[posicion.fila()][posicion.columna()];
    }

    public Posicion getPosicionJugador() {
        return jugador;
    }

    // El Builder configura el estado inicial por constructor; solo el núcleo mueve al jugador.
    void setPosicionJugador(Posicion posicion) {
        Objects.requireNonNull(posicion);
        if (!contiene(posicion)
                || !getCelda(posicion).getTerreno().esTransitablePorJugador()
                || getCelda(posicion).tieneCaja()) {
            throw new IllegalArgumentException(
                    "El jugador requiere una casilla libre y transitable");
        }
        jugador = posicion;
    }

    public List<Posicion> getPosicionesCajas() {
        List<Posicion> posiciones = new ArrayList<>();
        for (int fila = 0; fila < getAlto(); fila++) {
            for (int columna = 0; columna < getAncho(); columna++) {
                if (celdas[fila][columna].tieneCaja()) {
                    posiciones.add(new Posicion(fila, columna));
                }
            }
        }
        return List.copyOf(posiciones);
    }

    public Tablero copiar() {
        return new Tablero(celdas, jugador);
    }
}
