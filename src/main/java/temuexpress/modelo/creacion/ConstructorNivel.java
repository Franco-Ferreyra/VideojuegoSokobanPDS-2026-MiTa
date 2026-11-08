package temuexpress.modelo.creacion;

import temuexpress.modelo.nucleo.Nivel;
import temuexpress.modelo.nucleo.Posicion;

public interface ConstructorNivel {
    void reiniciar();

    void construirTablero(int ancho, int alto);

    void agregarTerreno(Posicion p, char simbolo);

    void agregarCaja(Posicion p, CreadorCaja creador);

    void colocarJugador(Posicion p);

    void vincularElementos();

    void configurarNivel(String numero, String region, String variante, int par);

    Nivel obtenerResultado();
}
