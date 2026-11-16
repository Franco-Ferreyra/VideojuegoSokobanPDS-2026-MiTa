package temuexpress.recursos;

import java.util.SplittableRandom;

/** Cinco nubes de la región que cruzan el cielo a distintas velocidades y vuelven a entrar. */
final class CapaNubes implements Capa {
    private static final int CANTIDAD = 5;
    private static final double[] VELOCIDADES = {0.5, 0.7, 1.0};

    private final Sprite[] nubes = new Sprite[CANTIDAD];
    private final double[] x = new double[CANTIDAD];
    private final double[] y = new double[CANTIDAD];
    private final double[] v = new double[CANTIDAD];

    /** Lee carpeta/anim/nube_1..5.png; las nubes van en la mitad de arriba de la banda. */
    CapaNubes(String carpeta, int desde, int hasta) {
        SplittableRandom azar = new SplittableRandom(carpeta.hashCode());
        for (int i = 0; i < CANTIDAD; i++) {
            nubes[i] = Sprite.leer(carpeta + "/anim/nube_" + (i + 1) + ".png");
            x[i] = azar.nextInt(Cuadro.ANCHO);
            y[i] = desde + azar.nextInt(Math.max(1, (hasta - desde) / 2));
            v[i] = VELOCIDADES[azar.nextInt(VELOCIDADES.length)];
        }
    }

    @Override
    public void pintar(Cuadro c) {
        for (int i = 0; i < CANTIDAD; i++) {
            int vuelta = Cuadro.ANCHO + nubes[i].ancho();
            double pos = (x[i] + c.numero() * v[i]) % vuelta - nubes[i].ancho();
            c.pegar(nubes[i], pos, y[i]);
        }
    }
}
