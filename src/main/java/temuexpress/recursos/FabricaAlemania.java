package temuexpress.recursos;

public final class FabricaAlemania implements FabricaNivel {
    public AspectoRepartidor crearRepartidor() {
        return new AspectoRepartidorAlemania();
    }

    public Escenario crearEscenario() {
        return new EscenarioAlemania();
    }

    public SonidoRegional crearSonido() {
        return new SonidoAlemania();
    }
}
