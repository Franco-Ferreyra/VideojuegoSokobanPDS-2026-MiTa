package temuexpress.recursos;

import java.util.Objects;

/** Sonido regional armado con el nombre de la música y la carpeta de voces. */
abstract class SonidoArchivos implements SonidoRegional {
    private final String musica;
    private final String carpetaVoces;

    SonidoArchivos(String musica, String carpetaVoces) {
        this.musica = "assets/audio/music/musica_" + Objects.requireNonNull(musica) + ".wav";
        this.carpetaVoces = "assets/audio/voices/" + Objects.requireNonNull(carpetaVoces) + "/";
    }

    public String getMusica() {
        return musica;
    }

    public String getCarpetaVoces() {
        return carpetaVoces;
    }
}
