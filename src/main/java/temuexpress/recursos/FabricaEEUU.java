package temuexpress.recursos;

public final class FabricaEEUU implements FabricaNivel {
    public AspectoRepartidor crearRepartidor() {
        return new AspectoRepartidorEEUU();
    }

    public Escenario crearEscenario() {
        return new EscenarioEEUU();
    }

    public SonidoRegional crearSonido() {
        return new SonidoEEUU();
    }
}
