package temuexpress.recursos;

public final class EscenarioAlemania extends EscenarioCarpeta {
    private static final int CIELO_DESDE = 42;
    private static final int CIELO_HASTA = 84;

    public EscenarioAlemania() {
        super("alemania");
    }

    /** Atardecer, nubes y un zepelín de TeMU cruzando despacio. */
    @Override
    public AnimacionFondo getAnimacion() {
        return new FondoAnimado(
                getFondo(),
                CIELO_DESDE,
                CIELO_HASTA,
                new CapaAtardecer(CIELO_DESDE, CIELO_HASTA),
                new CapaNubes(carpeta(), CIELO_DESDE, CIELO_HASTA),
                new CapaVolador(
                        "alemania/anim/zepelin.png",
                        t -> new double[] {440 - CapaVolador.avance(t, 360) * 520, 48}));
    }
}
