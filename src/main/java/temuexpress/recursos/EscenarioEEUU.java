package temuexpress.recursos;

public final class EscenarioEEUU extends EscenarioCarpeta {
    public EscenarioEEUU() {
        super("usa");
    }

    /** Noche en Las Vegas: reflectores, luces del cartel y la ciudad, y la avioneta de TeMU. */
    @Override
    public AnimacionFondo getAnimacion() {
        return new FondoAnimado(
                getFondo(),
                37,
                186,
                new CapaReflectores(70, 250, 385),
                new CapaLasVegas(),
                new CapaVolador(
                        "usa/anim/avioneta.png",
                        t -> {
                            double p = CapaVolador.avance(t, 180);
                            return new double[] {-60 + p * 520, 52 + 2 * Math.sin(p * 12)};
                        }));
    }
}
