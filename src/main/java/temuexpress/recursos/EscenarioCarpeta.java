package temuexpress.recursos;

import java.awt.Image;
import java.util.Objects;

/** Escenario que lee sus imágenes de una carpeta de assets/img. Lo comparten las 4 regiones. */
abstract class EscenarioCarpeta implements Escenario {
    private final String carpeta;

    EscenarioCarpeta(String carpeta) {
        this.carpeta = Objects.requireNonNull(carpeta);
    }

    String carpeta() {
        return carpeta;
    }

    private Image leer(String nombre) {
        return Imagenes.leer(carpeta + "/" + nombre + ".png");
    }

    public Image getFondo() {
        return leer("fondo");
    }

    public Image getSpritePared() {
        return leer("pared");
    }

    public Image getSpritePiso() {
        return leer("piso");
    }

    public Image getSpriteDestino() {
        return leer("destino");
    }

    public Image getSpriteMuroCerrado() {
        return leer("muro_cerrado");
    }

    public Image getSpriteMuroAbierto() {
        return leer("muro_abierto");
    }
}
