package demo;

import dto.EventoPartidaDTO;
import dto.ResumenNivelDTO;
import dto.TableroDTO;
import vista.HojaSprites;
import vista.OyenteVista;
import vista.VentanaJuego;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/**
 * DEMO TEMPORAL para probar la vista sin el back.
 * Hace de controlador falso con un mapa de prueba y reglas mínimas.
 * NO es parte del diseño: se borra cuando exista ControladorJuego.
 *
 * Ejecutar desde la raíz del repo (para encontrar assets/img).
 */
public class DemoVista implements OyenteVista {

    private static final String[] TERRENO_INICIAL = {
        "#############",
        "#    . W  ~ #",
        "# L    #  > #",
        "#  O   # 1 1#",
        "#   .  #    #",
        "#      #    #",
        "#############",
    };
    private static final String[] OBJETOS_INICIALES = {
        "             ",
        "             ",
        "   K 4       ",
        "  $          ",
        "   $         ",
        "     @       ",
        "             ",
    };
    private static final int PAR = 20;

    private final VentanaJuego ventana = new VentanaJuego();
    private char[][] terreno;
    private char[][] objetos;
    private int movimientos;
    private int empujes;
    private int usosUndo;
    private int usosUndoSeguidos;
    private int puntajeTotal;
    private boolean perdido;
    private int paso; // 0 inicio, 1 mensaje, 2 juego, 3 resumen, 4 final
    private final Deque<char[][][]> historial = new ArrayDeque<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DemoVista().arrancar());
    }

    private void arrancar() {
        ventana.setOyente(this);
        ventana.setSprites(cargarSprites("china", "repartidor_azul"));
        ventana.mostrarInicio();
        ventana.mostrar();
    }

    @Override
    public void alContinuar() {
        if (paso == 0) {
            paso = 1;
            ventana.mostrarMensaje("CHINA", new String[]{
                "La aduana bloqueó tu compra y pide tu documento para todo.",
                "Llevá la CAJA LLAVE hasta el CERROJO para abrir los muros.",
                "Si la sacás del cerrojo, te retienen el documento otra vez."});
        } else if (paso == 1) {
            paso = 2;
            reiniciarNivel();
            ventana.mostrarJuego(tablero());
            ventana.actualizar(tablero(), evento("MOVIMIENTO"));
        } else if (paso == 3) {
            paso = 4;
            ventana.mostrarFinal(new String[]{
                "TeMU Express cumplió: el paquete llegó",
                "de China a Sudamérica.",
                "Gracias por elegir TeMU Express."});
        } else if (paso == 4) {
            paso = 0;
            puntajeTotal = 0;
            ventana.setPuntajeTotal(0);
            ventana.mostrarInicio();
        }
    }

    @Override
    public void alPresionarFlecha(String direccion) {
        if (perdido) {
            return;
        }
        int df = 0;
        int dc = 0;
        switch (direccion) {
            case "ARRIBA": df = -1; break;
            case "ABAJO": df = 1; break;
            case "IZQUIERDA": dc = -1; break;
            default: dc = 1; break;
        }
        char[][][] antes = {copiar(terreno), copiar(objetos)};
        int[] j = buscarJugador();
        int f1 = j[0] + df;
        int c1 = j[1] + dc;
        if (!pisablePorJugador(terreno[f1][c1])) {
            return;
        }
        char obj = objetos[f1][c1];
        if (obj != ' ') {
            int f2 = f1 + df;
            int c2 = c1 + dc;
            if (!pisablePorCaja(terreno[f2][c2]) || objetos[f2][c2] != ' ') {
                return;
            }
            objetos[f1][c1] = ' ';
            if (obj >= '1' && obj <= '4') {
                obj--;
            }
            if (obj == '0') {
                perdido = true;
            } else if (terreno[f2][c2] == 'O') {
                terreno[f2][c2] = ' '; // la caja tapa el agujero
            } else {
                objetos[f2][c2] = obj;
            }
            empujes++;
        }
        objetos[j[0]][j[1]] = ' ';
        objetos[f1][c1] = '@';
        movimientos++;
        usosUndoSeguidos = 0;
        historial.push(antes);
        while (historial.size() > 15) {
            historial.removeLast();
        }
        actualizarMuros();
        if (perdido) {
            ventana.actualizar(tablero(), evento("DERROTA"));
            ventana.mostrarDerrota(new String[]{
                "Tu paquete no sobrevivió",
                "a la inspección."});
        } else if (gano()) {
            ventana.actualizar(tablero(), evento("VICTORIA"));
            int puntaje = Math.max(0, 1000 - 3 * movimientos - 5 * empujes - 100 * usosUndo)
                    + (usosUndo == 0 ? 200 : 0) + (movimientos <= PAR ? 150 : 0);
            puntajeTotal += puntaje;
            ventana.setPuntajeTotal(puntajeTotal);
            ventana.mostrarResumen(new ResumenNivelDTO(movimientos, empujes, usosUndo, PAR), puntaje);
            paso = 3;
        } else {
            ventana.actualizar(tablero(), evento("MOVIMIENTO"));
        }
    }

    @Override
    public void alPresionarUndo() {
        if (historial.isEmpty() || usosUndoSeguidos >= 3) {
            return;
        }
        char[][][] estado = null;
        for (int i = 0; i < 5 && !historial.isEmpty(); i++) {
            estado = historial.pop();
        }
        terreno = estado[0];
        objetos = estado[1];
        usosUndo++;
        usosUndoSeguidos++;
        perdido = false;
        ventana.mostrarJuego(tablero());
        ventana.actualizar(tablero(), evento("UNDO"));
    }

    @Override
    public void alPresionarReiniciar() {
        reiniciarNivel();
        ventana.mostrarJuego(tablero());
        ventana.actualizar(tablero(), evento("REINICIO"));
    }

    private void reiniciarNivel() {
        terreno = aMatriz(TERRENO_INICIAL);
        objetos = aMatriz(OBJETOS_INICIALES);
        movimientos = 0;
        empujes = 0;
        usosUndo = 0;
        usosUndoSeguidos = 0;
        perdido = false;
        historial.clear();
    }

    private void actualizarMuros() {
        boolean abiertos = true;
        for (int f = 0; f < terreno.length; f++) {
            for (int c = 0; c < terreno[f].length; c++) {
                if (terreno[f][c] == 'L' && objetos[f][c] != 'K') {
                    abiertos = false;
                }
            }
        }
        for (int f = 0; f < terreno.length; f++) {
            for (int c = 0; c < terreno[f].length; c++) {
                if (abiertos && terreno[f][c] == 'W') {
                    terreno[f][c] = 'w';
                } else if (!abiertos && terreno[f][c] == 'w' && objetos[f][c] == ' ') {
                    terreno[f][c] = 'W';
                }
            }
        }
    }

    private boolean gano() {
        for (int f = 0; f < objetos.length; f++) {
            for (int c = 0; c < objetos[f].length; c++) {
                char o = objetos[f][c];
                char t = terreno[f][c];
                if (o == 'K' && t != 'L') {
                    return false;
                }
                if ((o == '$' || (o >= '1' && o <= '4')) && t != '.') {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean pisablePorJugador(char t) {
        return t == ' ' || t == '.' || t == 'L' || t == 'w' || t == '~';
    }

    private static boolean pisablePorCaja(char t) {
        return t != '#' && t != 'W';
    }

    private int[] buscarJugador() {
        for (int f = 0; f < objetos.length; f++) {
            for (int c = 0; c < objetos[f].length; c++) {
                if (objetos[f][c] == '@') {
                    return new int[]{f, c};
                }
            }
        }
        return new int[]{1, 1};
    }

    private TableroDTO tablero() {
        String[] t = new String[terreno.length];
        String[] o = new String[objetos.length];
        for (int f = 0; f < terreno.length; f++) {
            t[f] = new String(terreno[f]);
            o[f] = new String(objetos[f]);
        }
        return new TableroDTO(terreno[0].length, terreno.length, t, o);
    }

    private EventoPartidaDTO evento(String tipo) {
        return new EventoPartidaDTO(tipo, "1-1", movimientos, empujes, usosUndoSeguidos);
    }

    private static char[][] aMatriz(String[] filas) {
        char[][] m = new char[filas.length][];
        for (int i = 0; i < filas.length; i++) {
            m[i] = filas[i].toCharArray();
        }
        return m;
    }

    private static char[][] copiar(char[][] m) {
        char[][] c = new char[m.length][];
        for (int i = 0; i < m.length; i++) {
            c[i] = m[i].clone();
        }
        return c;
    }

    /** Hace de fábrica regional falsa: lee los PNG de assets/img. */
    private static HojaSprites cargarSprites(String region, String repartidor) {
        HojaSprites s = new HojaSprites();
        String[] tiles = {"fondo", "pared", "piso", "destino", "muro_cerrado", "muro_abierto",
            "caja_normal", "caja_llave", "caja_fragil_1", "caja_fragil_2",
            "caja_fragil_3", "caja_fragil_4"};
        for (String t : tiles) {
            s.poner(t, leer("assets/img/" + region + "/" + t + ".png"));
        }
        s.poner("logo", leer("assets/img/logo.png"));
        for (String d : new String[]{"arriba", "abajo", "izquierda", "derecha", "esqueleto"}) {
            String clave = d.equals("esqueleto") ? d : "jugador_" + d;
            s.poner(clave, leer("assets/img/personaje/" + repartidor + "/" + d + ".png"));
        }
        return s;
    }

    private static java.awt.Image leer(String ruta) {
        try {
            return ImageIO.read(new File(ruta));
        } catch (IOException e) {
            System.err.println("No se encontró " + ruta);
            return null;
        }
    }
}
