package temuexpress.vista;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Locale;
import temuexpress.modelo.dto.TableroDTO;

/**
 * Dibuja el tablero de 13x7 en el lugar del boceto. Si el mapa es más chico, lo centra y completa
 * el resto con pared. No sabe nada de reglas.
 */
class VistaTablero {

    private TableroDTO tablero;
    private String direccionJugador = "abajo";

    void setTablero(TableroDTO tablero) {
        this.tablero = tablero;
    }

    void setDireccionJugador(String direccion) {
        this.direccionJugador = direccion.toLowerCase(Locale.ROOT);
    }

    void dibujar(Graphics2D g, HojaSprites sprites) {
        TableroDTO tablero = this.tablero;
        if (tablero == null) {
            return;
        }
        int t = Disposicion.TAM_CASILLA;
        int desdeColumna = (Disposicion.COLUMNAS - tablero.getAncho()) / 2;
        int desdeFila = (Disposicion.FILAS - tablero.getAlto()) / 2;
        String[] terreno = filas(tablero.getTerreno());
        String[] objetos = filas(tablero.getObjetos());
        for (int f = 0; f < Disposicion.FILAS; f++) {
            for (int c = 0; c < Disposicion.COLUMNAS; c++) {
                int x = Disposicion.TABLERO.x + c * t;
                int y = Disposicion.TABLERO.y + f * t;
                int fm = f - desdeFila;
                int cm = c - desdeColumna;
                if (!dentroDelMapa(tablero, fm, cm)) {
                    g.drawImage(sprites.get("pared"), x, y, null);
                    continue;
                }
                char ter = caracter(terreno, fm, cm);
                g.drawImage(sprites.get("piso"), x, y, null);
                if (ter != ' ') {
                    g.drawImage(sprites.get(claveTerreno(ter)), x, y, null);
                }
                String claveObjeto = claveObjeto(caracter(objetos, fm, cm));
                if (claveObjeto != null) {
                    Image img = sprites.get(claveObjeto);
                    g.drawImage(img, x, y, null);
                }
            }
        }
    }

    private static boolean dentroDelMapa(TableroDTO tablero, int f, int c) {
        return f >= 0 && c >= 0 && f < tablero.getAlto() && c < tablero.getAncho();
    }

    /** El DTO trae cada capa como un texto con una fila por línea. */
    private static String[] filas(String capa) {
        return capa == null ? new String[0] : capa.split("\n", -1);
    }

    private static char caracter(String[] filas, int f, int c) {
        if (f < filas.length && c < filas[f].length()) {
            return filas[f].charAt(c);
        }
        return ' ';
    }

    private static String claveTerreno(char ch) {
        switch (ch) {
            case '#':
                return "pared";
            case '.':
                return "destino";
            case 'L':
                return "cerrojo";
            case 'W':
                return "muro_cerrado";
            case 'w':
                return "muro_abierto";
            case 'O':
                return "agujero";
            case '~':
                return "hielo";
            case '>':
                return "cinta_derecha";
            case '<':
                return "cinta_izquierda";
            case '^':
                return "cinta_arriba";
            case 'v':
                return "cinta_abajo";
            default:
                if (ch >= '1' && ch <= '9') {
                    return "portal_" + ch;
                }
                return "piso";
        }
    }

    private String claveObjeto(char ch) {
        switch (ch) {
            case '@':
                return "jugador_" + direccionJugador;
            case '$':
                return "caja_normal";
            case 'K':
                return "caja_llave";
            case 'F':
                return "caja_fragil_4";
            case '1':
            case '2':
            case '3':
            case '4':
                return "caja_fragil_" + ch;
            default:
                return null;
        }
    }
}
