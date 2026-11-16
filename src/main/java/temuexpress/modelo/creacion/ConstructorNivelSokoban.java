package temuexpress.modelo.creacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import temuexpress.modelo.nucleo.Agujero;
import temuexpress.modelo.nucleo.Celda;
import temuexpress.modelo.nucleo.Cerrojo;
import temuexpress.modelo.nucleo.Cinta;
import temuexpress.modelo.nucleo.Destino;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Hielo;
import temuexpress.modelo.nucleo.Muro;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.modelo.nucleo.Pared;
import temuexpress.modelo.nucleo.Piso;
import temuexpress.modelo.nucleo.Portal;
import temuexpress.modelo.nucleo.Posicion;
import temuexpress.modelo.nucleo.Tablero;
import temuexpress.modelo.nucleo.Terreno;
import temuexpress.modelo.nucleo.TipoCaja;

public final class ConstructorNivelSokoban implements ConstructorNivel {
    private static final int MIN_DIMENSION = 3;
    private static final int MAX_ANCHO = 13;
    private static final int MAX_ALTO = 7;
    private Celda[][] celdas = new Celda[0][0];
    private Posicion jugador;
    private String numero, region, variante;
    private int par;

    public void reiniciar() {
        celdas = new Celda[0][0];
        jugador = null;
        numero = null;
        region = null;
        variante = null;
        par = -1;
    }

    public void configurarNivel(String numero, String region, String variante, int par) {
        if (numero == null || numero.isBlank() || par < 0)
            throw new IllegalArgumentException("Numero requerido y PAR no negativo");
        if (region == null || variante == null)
            throw new IllegalArgumentException("REGION y VARIANTE son obligatorias");
        boolean valida =
                switch (region) {
                    case "CHINA", "EEUU", "ALEMANIA" -> "NINGUNA".equals(variante);
                    case "SUDAMERICA" ->
                            Set.of("ARG-CHILE", "ECU-COL", "PERU-VEN").contains(variante);
                    default -> false;
                };
        if (!valida) throw new IllegalArgumentException("REGION o VARIANTE invalida");
        this.numero = numero;
        this.region = region;
        this.variante = variante;
        this.par = par;
    }

    public void construirTablero(int ancho, int alto) {
        if (ancho < MIN_DIMENSION || ancho > MAX_ANCHO || alto < MIN_DIMENSION || alto > MAX_ALTO)
            throw new IllegalArgumentException(
                    "Dimensiones permitidas: 3..13 columnas y 3..7 filas");
        celdas = new Celda[alto][ancho];
    }

    public void agregarTerreno(Posicion p, char simbolo) {
        Terreno terreno =
                switch (simbolo) {
                    case ' ' -> new Piso();
                    case '#' -> new Pared();
                    case '.' -> new Destino();
                    case 'L' -> new Cerrojo();
                    case 'W' -> new Muro();
                    case 'O' -> new Agujero();
                    case '~' -> new Hielo();
                    case '^' -> new Cinta(Direccion.ARRIBA);
                    case 'v' -> new Cinta(Direccion.ABAJO);
                    case '<' -> new Cinta(Direccion.IZQUIERDA);
                    case '>' -> new Cinta(Direccion.DERECHA);
                    case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> new Portal(simbolo - '0');
                    default ->
                            throw new IllegalArgumentException(
                                    "Simbolo de terreno desconocido: " + simbolo);
                };
        validarPosicion(p);
        if (celdas[p.fila()][p.columna()] != null)
            throw new IllegalStateException("La casilla ya tiene terreno");
        celdas[p.fila()][p.columna()] = new Celda(terreno);
    }

    public void agregarCaja(Posicion p, CreadorCaja creador) {
        Celda celda = obtenerCeldaConstruida(p);
        if (!celda.estaLibre() || p.equals(jugador) || esEspecial(celda.getTerreno()))
            throw new IllegalArgumentException(
                    "Caja sobre casilla ocupada, pared o terreno especial");
        celda.setCaja(creador.crearCaja());
    }

    public void colocarJugador(Posicion p) {
        if (jugador != null)
            throw new IllegalArgumentException("Debe haber exactamente un jugador");
        Celda celda = obtenerCeldaConstruida(p);
        if (!celda.estaLibre() || esEspecial(celda.getTerreno()))
            throw new IllegalArgumentException(
                    "Jugador sobre casilla ocupada, pared o terreno especial");
        jugador = p;
    }

    /** Los objetos arrancan solo sobre piso, destino, cerrojo o muro. */
    private static boolean esEspecial(Terreno terreno) {
        return terreno instanceof Agujero
                || terreno instanceof Hielo
                || terreno instanceof Cinta
                || terreno instanceof Portal;
    }

    private void validarPosicion(Posicion posicion) {
        if (celdas.length == 0)
            throw new IllegalStateException("Primero se debe construir el tablero");
        if (posicion == null
                || posicion.fila() < 0
                || posicion.fila() >= celdas.length
                || posicion.columna() < 0
                || posicion.columna() >= celdas[0].length) {
            throw new IllegalArgumentException("Posicion fuera del tablero: " + posicion);
        }
    }

    private Celda obtenerCeldaConstruida(Posicion posicion) {
        validarPosicion(posicion);
        Celda celda = celdas[posicion.fila()][posicion.columna()];
        if (celda == null) throw new IllegalStateException("Primero se debe agregar el terreno");
        return celda;
    }

    public void vincularElementos() {
        if (celdas.length == 0 || numero == null || jugador == null)
            throw new IllegalArgumentException(
                    "Nivel incompleto: falta tablero, encabezado o jugador");
        int cajas = 0, destinos = 0, llaves = 0, cerrojos = 0, muros = 0, agujeros = 0;
        for (int f = 0; f < celdas.length; f++)
            for (int c = 0; c < celdas[f].length; c++) {
                Celda celda = celdas[f][c];
                if (celda == null) throw new IllegalArgumentException("Terreno incompleto");
                boolean borde =
                        f == 0 || c == 0 || f == celdas.length - 1 || c == celdas[f].length - 1;
                if (borde && !(celda.getTerreno() instanceof Pared))
                    throw new IllegalArgumentException("El mapa debe estar rodeado de paredes");
                if (celda.tieneCaja()) {
                    if (celda.getCaja().getTipo() == TipoCaja.LLAVE) llaves++;
                    else cajas++;
                }
                if (celda.getTerreno().esDestino()) destinos++;
                if (celda.getTerreno().esCerrojo()) cerrojos++;
                if (celda.getTerreno() instanceof Muro) muros++;
                if (celda.getTerreno() instanceof Agujero) agujeros++;
            }
        if (cajas == 0 || cajas != destinos + agujeros)
            throw new IllegalArgumentException(
                    "Las cajas normales y fragiles deben ser tantas como destinos + agujeros");
        if (llaves != cerrojos)
            throw new IllegalArgumentException("Debe haber tantas cajas llave como cerrojos");
        if (muros > 0 && cerrojos == 0)
            throw new IllegalArgumentException("Los muros necesitan al menos un cerrojo");
        vincularPortales();
        validarSinCiclos();
    }

    /** Los portales van de a pares con el mismo número; cada uno conoce dónde está su pareja. */
    private void vincularPortales() {
        Map<Integer, List<Posicion>> pares = new HashMap<>();
        for (int f = 0; f < celdas.length; f++)
            for (int c = 0; c < celdas[f].length; c++)
                if (celdas[f][c].getTerreno() instanceof Portal portal)
                    pares.computeIfAbsent(portal.getIdPar(), k -> new ArrayList<>())
                            .add(new Posicion(f, c));
        for (Map.Entry<Integer, List<Posicion>> par : pares.entrySet()) {
            List<Posicion> posiciones = par.getValue();
            if (posiciones.size() != 2)
                throw new IllegalArgumentException(
                        "El portal " + par.getKey() + " no tiene pareja");
            portalEn(posiciones.get(0)).setPareja(posiciones.get(1));
            portalEn(posiciones.get(1)).setPareja(posiciones.get(0));
        }
    }

    private Portal portalEn(Posicion p) {
        return (Portal) celdas[p.fila()][p.columna()].getTerreno();
    }

    private record Paso(Posicion posicion, Direccion direccion) {}

    /**
     * Rechaza cintas, hielo y portales que formen un ciclo (una caja daría vueltas para siempre).
     * Se recorre con el tablero vacío y los muros abiertos, que es el peor caso.
     */
    private void validarSinCiclos() {
        for (int f = 0; f < celdas.length; f++)
            for (int c = 0; c < celdas[f].length; c++)
                for (Direccion dir : Direccion.values()) recorrer(new Posicion(f, c), dir);
    }

    private void recorrer(Posicion inicio, Direccion dirInicial) {
        Set<Paso> vistos = new HashSet<>();
        Posicion actual = inicio;
        Direccion dir = dirInicial;
        while (true) {
            if (!vistos.add(new Paso(actual, dir)))
                throw new IllegalArgumentException("Cintas, hielo o portales forman un ciclo");
            Terreno terreno = celdas[actual.fila()][actual.columna()].getTerreno();
            Posicion siguiente;
            if (terreno instanceof Hielo) {
                siguiente = actual.siguiente(dir);
            } else if (terreno instanceof Cinta cinta) {
                dir = cinta.getDireccion();
                siguiente = actual.siguiente(dir);
            } else if (terreno instanceof Portal portal) {
                siguiente = portal.getPareja().siguiente(dir);
            } else {
                return;
            }
            if (siguiente.fila() < 0
                    || siguiente.fila() >= celdas.length
                    || siguiente.columna() < 0
                    || siguiente.columna() >= celdas[0].length
                    || celdas[siguiente.fila()][siguiente.columna()].getTerreno()
                            instanceof Pared) {
                return;
            }
            actual = siguiente;
        }
    }

    public Nivel obtenerResultado() {
        vincularElementos();
        return new Nivel(numero, region, variante, par, new Tablero(celdas, jugador));
    }
}
