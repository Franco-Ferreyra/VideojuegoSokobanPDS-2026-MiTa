package temuexpress.recursos;

/**
 * Abstract Factory: crea los productos de una región (repartidor, escenario y música) que tienen
 * que combinar entre sí. Las cajas no son regionales.
 */
public interface FabricaNivel {
    AspectoRepartidor crearRepartidor();

    Escenario crearEscenario();

    SonidoRegional crearSonido();
}
