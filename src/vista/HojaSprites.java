package vista;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Imágenes que usa la vista, por clave. Las carga el controlador con lo que
 * entrega la fábrica de la región (Escenario y AspectoRepartidor), así la
 * vista no depende del paquete recursos.
 *
 * Claves:
 *   terreno: pared, piso, destino, cerrojo, muro_cerrado, muro_abierto,
 *            agujero, hielo, cinta_arriba/abajo/izquierda/derecha, portal_1..9
 *   objetos: caja_normal, caja_llave, caja_fragil_1..4,
 *            jugador_arriba/abajo/izquierda/derecha, esqueleto
 *   pantalla: fondo (boceto de la región), logo (portada)
 *
 * Si falta una imagen, devuelve un reemplazo provisorio para que se vea algo.
 */
public class HojaSprites {

    public static final int TAM = 16;

    private final Map<String, Image> imagenes = new HashMap<>();
    private final Map<String, Image> provisorias = new HashMap<>();

    public void poner(String clave, Image imagen) {
        if (imagen != null) {
            imagenes.put(clave, imagen);
        }
    }

    public boolean tiene(String clave) {
        return imagenes.containsKey(clave);
    }

    public Image get(String clave) {
        Image img = imagenes.get(clave);
        if (img != null) {
            return img;
        }
        return provisorias.computeIfAbsent(clave, HojaSprites::crearProvisoria);
    }

    private static Image crearProvisoria(String clave) {
        BufferedImage im = new BufferedImage(TAM, TAM, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        if (clave.equals("cerrojo")) {
            g.setColor(new Color(90, 70, 30));
            g.fillRect(2, 2, 12, 12);
            g.setColor(new Color(255, 206, 0));
            g.fillRect(4, 4, 8, 8);
            g.setColor(new Color(90, 70, 30));
            g.fillRect(7, 6, 2, 5);
        } else if (clave.equals("agujero")) {
            g.setColor(new Color(60, 50, 40));
            g.fillRect(1, 1, 14, 14);
            g.setColor(new Color(10, 8, 6));
            g.fillRect(3, 3, 10, 10);
        } else if (clave.equals("hielo")) {
            g.setColor(new Color(200, 240, 255));
            g.fillRect(0, 0, TAM, TAM);
            g.setColor(Color.WHITE);
            g.drawLine(3, 12, 12, 3);
        } else if (clave.startsWith("cinta_")) {
            g.setColor(new Color(70, 70, 80));
            g.fillRect(0, 0, TAM, TAM);
            g.setColor(new Color(255, 206, 0));
            dibujarFlecha(g, clave.substring(6));
        } else if (clave.startsWith("portal_")) {
            int n = clave.charAt(clave.length() - 1) - '0';
            Color[] colores = {Color.CYAN, Color.ORANGE, Color.MAGENTA, Color.GREEN,
                Color.PINK, Color.YELLOW, Color.RED, Color.BLUE, Color.WHITE};
            g.setColor(Color.BLACK);
            g.fillOval(1, 1, 14, 14);
            g.setColor(colores[Math.max(0, Math.min(8, n - 1))]);
            g.drawOval(2, 2, 11, 11);
            g.drawOval(4, 4, 7, 7);
        } else if (clave.equals("fondo") || clave.equals("logo")) {
            g.dispose();
            return null;
        } else {
            g.setColor(Color.MAGENTA);
            g.fillRect(0, 0, TAM, TAM);
        }
        g.dispose();
        return im;
    }

    private static void dibujarFlecha(Graphics2D g, String dir) {
        int[][] puntos;
        switch (dir) {
            case "arriba":
                puntos = new int[][]{{8, 3}, {3, 9}, {13, 9}};
                break;
            case "abajo":
                puntos = new int[][]{{8, 13}, {3, 7}, {13, 7}};
                break;
            case "izquierda":
                puntos = new int[][]{{3, 8}, {9, 3}, {9, 13}};
                break;
            default:
                puntos = new int[][]{{13, 8}, {7, 3}, {7, 13}};
        }
        g.fillPolygon(new int[]{puntos[0][0], puntos[1][0], puntos[2][0]},
                new int[]{puntos[0][1], puntos[1][1], puntos[2][1]}, 3);
    }
}
