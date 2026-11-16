package temuexpress.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import temuexpress.modelo.dto.EventoPartidaDTO;
import temuexpress.modelo.observador.ObservadorPartida;
import temuexpress.recursos.GestorSonido;

/**
 * Observer: cada evento de la partida tiene su efecto de sonido, y algunos la voz regional. Un
 * movimiento puede generar varios eventos seguidos; con la prioridad, suena solo el más importante
 * (victoria y derrota antes que una entrega, una entrega antes que un paso).
 */
public final class ReaccionAudioPartida implements ObservadorPartida {
    /**
     * Para lo que tiene que sonar siempre: el mensaje de inicio, ganar, perder, Undo, Reiniciar.
     */
    static final int PRIORIDAD_MAXIMA = 3;

    private static final String EFECTOS = "assets/audio/sfx/";

    private record Sonido(String archivo, int prioridad) {}

    private static final Map<String, Sonido> EFECTO_POR_TIPO =
            Map.ofEntries(
                    Map.entry("MOVIMIENTO", new Sonido("paso", 0)),
                    Map.entry("EMPUJE", new Sonido("empuje", 0)),
                    Map.entry("RESBALAR", new Sonido("resbalar", 1)),
                    Map.entry("CINTA", new Sonido("cinta", 1)),
                    Map.entry("PORTAL", new Sonido("portal", 1)),
                    Map.entry("FRAGIL_DANO", new Sonido("fragil_dano", 1)),
                    Map.entry("CAIDA", new Sonido("agujero_cae", 2)),
                    Map.entry("ROTURA", new Sonido("fragil_rompe", 2)),
                    Map.entry("META", new Sonido("meta_cumplida", 2)),
                    Map.entry("MURO_ABRE", new Sonido("muro_abre", 2)),
                    Map.entry("MURO_CIERRA", new Sonido("muro_cierra", 2)),
                    Map.entry("REINICIO", new Sonido("reiniciar", PRIORIDAD_MAXIMA)));

    /**
     * Victoria, derrota y Undo tienen solo la voz (sin efecto encima). La entrega no tiene voz: lo
     * que dice el repartidor sale en el globo, y así no se pisa con lo que viene después.
     */
    private static final Map<String, Sonido> VOZ_POR_TIPO =
            Map.of(
                    "MURO_ABRE", new Sonido("desbloqueo", 2),
                    "VICTORIA", new Sonido("victoria", PRIORIDAD_MAXIMA),
                    "DERROTA", new Sonido("derrota", PRIORIDAD_MAXIMA),
                    "UNDO", new Sonido("undo", PRIORIDAD_MAXIMA));

    private final String carpetaVoces;

    public ReaccionAudioPartida(String carpetaVoces) {
        this.carpetaVoces = Objects.requireNonNull(carpetaVoces);
    }

    /**
     * Abre de antemano todos los sonidos del nivel (y la voz de inicio), así suenan al instante.
     */
    public void precargar() {
        List<String> rutas = new ArrayList<>();
        EFECTO_POR_TIPO.values().forEach(s -> rutas.add(EFECTOS + s.archivo() + ".wav"));
        VOZ_POR_TIPO.values().forEach(s -> rutas.add(carpetaVoces + s.archivo() + ".wav"));
        rutas.add(carpetaVoces + "inicio.wav");
        GestorSonido.getInstancia().precargar(rutas);
    }

    @Override
    public void actualizar(EventoPartidaDTO evento) {
        GestorSonido sonido = GestorSonido.getInstancia();
        Sonido efecto = EFECTO_POR_TIPO.get(evento.getTipo());
        if (efecto != null) {
            sonido.reproducirEfecto(EFECTOS + efecto.archivo() + ".wav", efecto.prioridad());
        }
        Sonido voz = VOZ_POR_TIPO.get(evento.getTipo());
        if (voz != null) {
            sonido.reproducirVoz(carpetaVoces + voz.archivo() + ".wav", voz.prioridad());
        }
    }
}
