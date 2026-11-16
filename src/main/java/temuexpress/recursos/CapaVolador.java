package temuexpress.recursos;

import java.util.List;

/**
 * Algo que vuela por el cielo (avión, zepelín, farolito, loro, cóndor): sus imágenes se alternan y
 * el Recorrido dice dónde está en cada cuadro.
 */
final class CapaVolador implements Capa {

    /** Posición {x, y} en el cuadro t. */
    @FunctionalInterface
    interface Recorrido {
        double[] posicion(int t);
    }

    private final List<Sprite> imagenes;
    private final int cuadrosPorImagen;
    private final Recorrido recorrido;

    CapaVolador(List<Sprite> imagenes, int cuadrosPorImagen, Recorrido recorrido) {
        this.imagenes = List.copyOf(imagenes);
        this.cuadrosPorImagen = Math.max(1, cuadrosPorImagen);
        this.recorrido = recorrido;
    }

    /** Una sola imagen. */
    CapaVolador(String ruta, Recorrido recorrido) {
        this(List.of(Sprite.leer(ruta)), 1, recorrido);
    }

    /** Avance de 0 a 1 de un recorrido que dura {@code periodo} cuadros y se repite. */
    static double avance(int t, int periodo) {
        return Math.floorMod(t, periodo) / (double) periodo;
    }

    @Override
    public void pintar(Cuadro c) {
        int t = c.numero();
        Sprite actual = imagenes.get(Math.floorMod(t / cuadrosPorImagen, imagenes.size()));
        double[] p = recorrido.posicion(t);
        c.pegar(actual, p[0], p[1]);
    }
}
