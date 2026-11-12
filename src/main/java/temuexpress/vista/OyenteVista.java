package temuexpress.vista;

/**
 * Lo que la vista avisa cuando el jugador toca una tecla o un botón. Lo implementa el controlador.
 * La vista no conoce el modelo.
 *
 * <p>direccion: "ARRIBA", "ABAJO", "IZQUIERDA" o "DERECHA" (los mismos nombres del enum Direccion
 * del modelo).
 */
public interface OyenteVista {

    void alPresionarFlecha(String direccion);

    void alPresionarUndo();

    void alPresionarReiniciar();

    /** ENTER o clic en las pantallas de inicio, mensaje, resumen y final. */
    void alContinuar();
}
