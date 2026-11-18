package temuexpress.controlador;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import temuexpress.modelo.creacion.ConstructorNivelSokoban;
import temuexpress.modelo.creacion.LectorNivel;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.recursos.AnimacionFondo;
import temuexpress.recursos.AspectoRepartidor;
import temuexpress.recursos.Escenario;
import temuexpress.recursos.FabricaAlemania;
import temuexpress.recursos.FabricaChina;
import temuexpress.recursos.FabricaEEUU;
import temuexpress.recursos.FabricaNivel;
import temuexpress.recursos.FabricaSudamerica;
import temuexpress.recursos.GestorSonido;
import temuexpress.recursos.Imagenes;
import temuexpress.vista.HojaSprites;

/**
 * Facade: el controlador pide un nivel con prepararNivel(numero) y la fachada coordina el Builder
 * (LectorNivel), la fábrica regional (Abstract Factory) y la música (GestorSonido). Después le da
 * al controlador lo que la vista necesita de esa región (sprites, fondo animado y carpeta de voces)
 * sin exponer la fábrica.
 */
public final class PreparadorNivelFacade {
    private static final String CARPETA_NIVELES = "niveles";

    /** Cajas y mecánicas son iguales en todas las regiones: se toman de una sola carpeta. */
    private static final String CARPETA_COMUN = "china/";

    private static final List<String> SPRITES_COMUNES =
            List.of(
                    "caja_normal",
                    "caja_llave",
                    "caja_fragil_1",
                    "caja_fragil_2",
                    "caja_fragil_3",
                    "caja_fragil_4",
                    "cerrojo",
                    "agujero",
                    "hielo",
                    "cinta_arriba",
                    "cinta_abajo",
                    "cinta_izquierda",
                    "cinta_derecha");

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

    /** Imágenes de la vista: los productos de la fábrica regional más las cajas y mecánicas. */
    public HojaSprites armarSprites() {
        FabricaNivel fabrica = fabricaPreparada();
        HojaSprites sprites = new HojaSprites();
        Escenario escenario = fabrica.crearEscenario();
        sprites.poner("fondo", escenario.getFondo());
        sprites.poner("pared", escenario.getSpritePared());
        sprites.poner("piso", escenario.getSpritePiso());
        sprites.poner("destino", escenario.getSpriteDestino());
        sprites.poner("muro_cerrado", escenario.getSpriteMuroCerrado());
        sprites.poner("muro_abierto", escenario.getSpriteMuroAbierto());
        AspectoRepartidor repartidor = fabrica.crearRepartidor();
        for (Direccion dir : Direccion.values()) {
            sprites.poner(
                    "jugador_" + dir.name().toLowerCase(Locale.ROOT), repartidor.getSprite(dir));
        }
        sprites.poner("esqueleto", repartidor.getEsqueleto());
        for (String clave : SPRITES_COMUNES) {
            sprites.poner(clave, Imagenes.leer(CARPETA_COMUN + clave + ".png"));
        }
        for (int n = 1; n <= 9; n++) {
            sprites.poner("portal_" + n, Imagenes.leer(CARPETA_COMUN + "portal_" + n + ".png"));
        }
        sprites.poner("logo", Imagenes.leer("logo.png"));
        return sprites;
    }

    /** El fondo con el cielo animado de la región. */
    public AnimacionFondo getAnimacionFondo() {
        return fabricaPreparada().crearEscenario().getAnimacion();
    }

    /** Carpeta de las voces de la región; las reproduce GestorSonido. */
    public String getCarpetaVoces() {
        return fabricaPreparada().crearSonido().getCarpetaVoces();
    }

    private FabricaNivel fabricaPreparada() {
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
