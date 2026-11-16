package temuexpress.recursos;

public final class FabricaChina implements FabricaNivel {
    public AspectoRepartidor crearRepartidor() {
        return new AspectoRepartidorChina();
    }

    public Escenario crearEscenario() {
        return new EscenarioChina();
    }

    public SonidoRegional crearSonido() {
        return new SonidoChina();
    }
}
