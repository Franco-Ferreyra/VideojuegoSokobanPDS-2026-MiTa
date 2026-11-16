package temuexpress.modelo.creacion;

import java.io.IOException;
import java.nio.file.Path;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.recursos.FabricaAlemania;
import temuexpress.recursos.FabricaChina;
import temuexpress.recursos.FabricaEEUU;
import temuexpress.recursos.FabricaNivel;
import temuexpress.recursos.FabricaSudamerica;
import temuexpress.recursos.GestorSonido;

/**
 * Facade: el controlador pide un nivel con prepararNivel(numero) y la fachada coordina el Builder
 * (LectorNivel), la fábrica regional (Abstract Factory) y la música (GestorSonido).
 */
public final class PreparadorNivelFacade {
    private static final String CARPETA_NIVELES = "niveles";
    private final LectorNivel lector = new LectorNivel(new ConstructorNivelSokoban());
    private final GestorSonido sonido = GestorSonido.getInstancia();
    private FabricaNivel fabricaActual;

    public Nivel prepararNivel(String numero) throws IOException {
        Nivel nivel =
                lector.construir(Path.of(CARPETA_NIVELES, "nivel_" + numero + ".txt"), numero);
        fabricaActual = crearFabrica(nivel.getRegion(), nivel.getVariante());
        sonido.reproducirMusica(fabricaActual.crearSonido().getMusica());
        return nivel;
    }

    /** Fábrica de la región del último nivel preparado (para armar las imágenes de la vista). */
    public FabricaNivel getFabricaActual() {
        if (fabricaActual == null)
            throw new IllegalStateException("Todavia no se preparo un nivel");
        return fabricaActual;
    }

    private static FabricaNivel crearFabrica(String region, String variante) {
        return switch (region) {
            case "CHINA" -> new FabricaChina();
            case "EEUU" -> new FabricaEEUU();
            case "ALEMANIA" -> new FabricaAlemania();
            case "SUDAMERICA" -> new FabricaSudamerica(variante);
            default -> throw new IllegalArgumentException("Region sin fabrica: " + region);
        };
    }
}
