package temuexpress.recursos;

/** Reflectores que barren el cielo desde abajo, por detrás del cartel y los edificios. */
final class CapaReflectores implements Capa {
    private static final int PERIODO = 60;
    private static final int LARGO = 150;
    private static final int LUZ = 0xFFF5DC;
    private static final int PIE = 178;
    private static final int TECHO = 42;

    private final int[] origenes;

    /** Posición x de cada reflector, al pie de la ciudad. */
    CapaReflectores(int... origenes) {
        this.origenes = origenes.clone();
    }

    @Override
    public void pintar(Cuadro c) {
        for (int i = 0; i < origenes.length; i++) {
            double fase = i * 2.1;
            double ang = -Math.PI / 2 + 0.45 * Math.sin(2 * Math.PI * c.numero() / PERIODO + fase);
            double cos = Math.cos(ang);
            double sen = Math.sin(ang);
            for (int paso = 0; paso < LARGO; paso++) {
                double cx = origenes[i] + cos * paso;
                double cy = PIE + sen * paso;
                int ancho = (int) (1 + paso * 0.07);
                for (int d = -ancho; d <= ancho; d++) {
                    int x = (int) (cx - d * sen);
                    int y = (int) (cy + d * cos);
                    if (y >= TECHO && y < PIE && c.esCielo(x, y)) c.mezclar(x, y, LUZ, 0.22);
                }
            }
        }
    }
}
