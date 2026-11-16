package temuexpress.recursos;

/** Producto: música de la región y carpeta de sus voces. */
public interface SonidoRegional {
    String getMusica();

    /** Carpeta de assets/audio/voices de la región; las voces las reproduce GestorSonido. */
    String getCarpetaVoces();
}
