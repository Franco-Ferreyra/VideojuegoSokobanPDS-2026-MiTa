package dto;

/**
 * Foto del tablero para la vista (diagrama 0). Inmutable y sin reglas.
 *
 * Cada fila es un String de "ancho" caracteres.
 *
 * Capa terreno:
 *   '#' pared, ' ' piso, '.' destino, 'L' cerrojo,
 *   'W' muro cerrado, 'w' muro abierto, 'O' agujero (abierto),
 *   '~' hielo, '>' '<' '^' 'v' cinta, '1'-'9' portal (un dígito por par).
 *   Un agujero tapado se informa como piso (' ').
 *
 * Capa objetos:
 *   ' ' vacío, '@' jugador, '$' caja normal, 'K' caja llave,
 *   '4' '3' '2' '1' caja frágil con esa resistencia.
 */
public class TableroDTO {

    private final int ancho;
    private final int alto;
    private final String[] terreno;
    private final String[] objetos;

    public TableroDTO(int ancho, int alto, String[] terreno, String[] objetos) {
        this.ancho = ancho;
        this.alto = alto;
        this.terreno = terreno.clone();
        this.objetos = objetos.clone();
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public String[] getTerreno() {
        return terreno.clone();
    }

    public String[] getObjetos() {
        return objetos.clone();
    }
}
