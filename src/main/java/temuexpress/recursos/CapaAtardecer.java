package temuexpress.recursos;

/** El cielo del atardecer se aclara y se oscurece muy suave. */
final class CapaAtardecer implements Capa {
    private static final int PERIODO = 120;
    private final int desde;
    private final int hasta;

    CapaAtardecer(int desde, int hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    @Override
    public void pintar(Cuadro c) {
        double k = 1 + 0.05 * Math.sin(2 * Math.PI * c.numero() / PERIODO);
        for (int y = desde; y < hasta; y++) {
            for (int x = 0; x < Cuadro.ANCHO; x++) {
                if (c.esCielo(x, y)) c.fijar(x, y, Cuadro.brillo(c.base(x, y), k));
            }
        }
    }
}
