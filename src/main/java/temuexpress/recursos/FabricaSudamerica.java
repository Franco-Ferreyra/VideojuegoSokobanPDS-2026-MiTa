package temuexpress.recursos;

import java.util.Objects;

/** Fábrica de Sudamérica: recibe la variante (ARG-CHILE, ECU-COL o PERU-VEN). */
public final class FabricaSudamerica implements FabricaNivel {
    private final String carpeta;

    public FabricaSudamerica(String variante) {
        carpeta =
                switch (Objects.requireNonNull(variante)) {
                    case "ARG-CHILE" -> "arg_chile";
                    case "ECU-COL" -> "ecuador_colombia";
                    case "PERU-VEN" -> "peru_venezuela";
                    default -> throw new IllegalArgumentException("Variante invalida: " + variante);
                };
    }

    public AspectoRepartidor crearRepartidor() {
        return new AspectoRepartidorSudamerica();
    }

    public Escenario crearEscenario() {
        return new EscenarioSudamerica(carpeta);
    }

    public SonidoRegional crearSonido() {
        return new SonidoSudamerica(carpeta);
    }
}
