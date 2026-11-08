package temuexpress.modelo.creacion;

import temuexpress.modelo.nucleo.Celda;
import temuexpress.modelo.nucleo.Destino;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.modelo.nucleo.Pared;
import temuexpress.modelo.nucleo.Piso;
import temuexpress.modelo.nucleo.Posicion;
import temuexpress.modelo.nucleo.Tablero;
import temuexpress.modelo.nucleo.Terreno;

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
                            java.util.Set.of("ARG-CHILE", "ECU-COL", "PERU-VEN").contains(variante);
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
                    case 'L',
                                    'W',
                                    'O',
                                    '~',
                                    '>',
                                    '<',
                                    '^',
                                    'v',
                                    '1',
                                    '2',
                                    '3',
                                    '4',
                                    '5',
                                    '6',
                                    '7',
                                    '8',
                                    '9' ->
                            throw new IllegalArgumentException(
                                    "Mecanica pendiente de implementar: " + simbolo);
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
        if (!celda.estaLibre() || p.equals(jugador))
            throw new IllegalArgumentException("Caja sobre casilla ocupada o pared");
        celda.setCaja(creador.crearCaja());
    }

    public void colocarJugador(Posicion p) {
        if (jugador != null)
            throw new IllegalArgumentException("Debe haber exactamente un jugador");
        if (!obtenerCeldaConstruida(p).estaLibre())
            throw new IllegalArgumentException("Jugador sobre casilla ocupada o pared");
        jugador = p;
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
        int cajas = 0, destinos = 0;
        for (int f = 0; f < celdas.length; f++)
            for (int c = 0; c < celdas[f].length; c++) {
                Celda celda = celdas[f][c];
                if (celda == null) throw new IllegalArgumentException("Terreno incompleto");
                boolean borde =
                        f == 0 || c == 0 || f == celdas.length - 1 || c == celdas[f].length - 1;
                if (borde && !(celda.getTerreno() instanceof Pared))
                    throw new IllegalArgumentException("El mapa debe estar rodeado de paredes");
                if (celda.tieneCaja()) cajas++;
                if (celda.getTerreno().esDestino()) destinos++;
            }
        if (cajas == 0 || cajas != destinos)
            throw new IllegalArgumentException(
                    "Debe haber al menos una caja y tantas cajas como destinos");
    }

    public Nivel obtenerResultado() {
        vincularElementos();
        return new Nivel(numero, region, variante, par, new Tablero(celdas, jugador));
    }
}
