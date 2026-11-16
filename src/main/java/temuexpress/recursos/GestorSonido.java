package temuexpress.recursos;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Singleton (pedido por el profesor): un único reproductor de audio para todo el juego, así la
 * música de una región no se superpone con la de otra. Si no hay placa de sonido o falta un
 * archivo, el juego sigue sin sonido.
 *
 * <p>Todo el audio corre en un hilo propio, de a una tarea por vez, así el juego nunca espera a la
 * placa de sonido. Cada sonido se abre una sola vez (al cargar el nivel) y después solo se rebobina
 * y se reproduce: suena al instante.
 */
public enum GestorSonido {
    INSTANCIA;

    /** Las voces suenan por encima de todo; los efectos, un poco más bajos. */
    private static final float DB_VOZ = 6f;

    private static final float DB_EFECTO = -6f;

    /** Mientras habla el repartidor, la música baja todavía más. */
    private static final float DB_MUSICA_CON_VOZ = -12f;

    private final ExecutorService hilo =
            Executors.newSingleThreadExecutor(
                    tarea -> {
                        Thread t = new Thread(tarea, "audio");
                        t.setDaemon(true);
                        return t;
                    });

    // Estos campos solo se tocan desde el hilo de audio.
    private final Map<String, Clip> abiertos = new HashMap<>();
    private Clip musica;
    private String musicaActual;
    private int volumen = 30;
    private Clip voz;
    private int prioridadVoz;
    private Clip efecto;
    private int prioridadEfecto;

    /** Enum de un solo valor: la forma más segura de Singleton en Java (Effective Java). */
    public static GestorSonido getInstancia() {
        return INSTANCIA;
    }

    /**
     * Deja abiertos los sonidos del nivel que empieza y cierra los que ya no se usan (por ejemplo,
     * las voces de la región anterior).
     */
    public void precargar(Collection<String> rutas) {
        Set<String> nuevas = new HashSet<>(rutas);
        hilo.execute(
                () -> {
                    abiertos.entrySet()
                            .removeIf(
                                    e -> {
                                        if (nuevas.contains(e.getKey())) return false;
                                        cerrar(e.getValue());
                                        return true;
                                    });
                    for (String ruta : nuevas) clip(ruta);
                });
    }

    /** Música en loop. Si ya está sonando la misma, no la reinicia. */
    public void reproducirMusica(String ruta) {
        hilo.execute(
                () -> {
                    if (ruta.equals(musicaActual) && musica != null && musica.isOpen()) return;
                    cerrar(musica);
                    musica = abrir(ruta);
                    musicaActual = ruta;
                    if (musica != null) {
                        ajustarMusica();
                        musica.loop(Clip.LOOP_CONTINUOUSLY);
                    }
                });
    }

    public void detenerMusica() {
        hilo.execute(
                () -> {
                    cerrar(musica);
                    musica = null;
                    musicaActual = null;
                });
    }

    /**
     * Un solo efecto a la vez. Si ya suena uno de mayor prioridad, el nuevo no se reproduce; si no,
     * lo reemplaza.
     */
    public void reproducirEfecto(String ruta, int prioridad) {
        hilo.execute(
                () -> {
                    if (efecto != null && prioridad < prioridadEfecto) return;
                    Clip clip = clip(ruta);
                    if (clip == null) return;
                    parar(efecto);
                    aplicarGanancia(clip, DB_EFECTO);
                    efecto = clip;
                    prioridadEfecto = prioridad;
                    desdeElPrincipio(clip);
                });
    }

    /**
     * La voz del repartidor: más fuerte, y baja la música mientras suena. Nunca hablan dos voces a
     * la vez (misma regla de prioridad que los efectos), y la voz corta el efecto que esté sonando
     * para que se entienda.
     */
    public void reproducirVoz(String ruta, int prioridad) {
        hilo.execute(
                () -> {
                    if (voz != null && prioridad < prioridadVoz) return;
                    Clip clip = clip(ruta);
                    if (clip == null) return;
                    parar(voz);
                    parar(efecto);
                    efecto = null;
                    aplicarGanancia(clip, DB_VOZ);
                    voz = clip;
                    prioridadVoz = prioridad;
                    ajustarMusica();
                    desdeElPrincipio(clip);
                });
    }

    /** Corta la voz que esté sonando (por ejemplo, la del mensaje de inicio al empezar a jugar). */
    public void detenerVoz() {
        hilo.execute(
                () -> {
                    parar(voz);
                    voz = null;
                    ajustarMusica();
                });
    }

    /** Volumen de la música, de 0 a 100. */
    public void setVolumen(int v) {
        hilo.execute(
                () -> {
                    volumen = Math.max(0, Math.min(100, v));
                    ajustarMusica();
                });
    }

    /** El clip ya abierto de ese archivo, o lo abre y lo guarda. */
    private Clip clip(String ruta) {
        Clip clip = abiertos.get(ruta);
        if (clip == null) {
            clip = abrir(ruta);
            if (clip == null) return null;
            Clip guardado = clip;
            // Java Sound avisa desde otro hilo; la tarea se pasa al hilo de audio.
            clip.addLineListener(
                    e -> {
                        if (e.getType() == LineEvent.Type.STOP)
                            hilo.execute(() -> termino(guardado));
                    });
            abiertos.put(ruta, clip);
        }
        return clip;
    }

    private void termino(Clip clip) {
        if (clip.isRunning()) return; // se volvió a reproducir
        if (clip == voz) {
            voz = null;
            ajustarMusica();
        }
        if (clip == efecto) efecto = null;
    }

    private void ajustarMusica() {
        if (musica == null) return;
        if (volumen == 0) {
            aplicarGanancia(musica, Float.NEGATIVE_INFINITY);
            return;
        }
        float db = (float) (20 * Math.log10(volumen / 100.0));
        aplicarGanancia(musica, voz != null ? db + DB_MUSICA_CON_VOZ : db);
    }

    private static void desdeElPrincipio(Clip clip) {
        clip.stop();
        clip.flush();
        clip.setFramePosition(0);
        clip.start();
    }

    private static void parar(Clip clip) {
        if (clip != null && clip.isOpen()) clip.stop();
    }

    private static void cerrar(Clip clip) {
        if (clip == null || !clip.isOpen()) return;
        clip.stop();
        clip.close();
    }

    private static Clip abrir(String ruta) {
        try (AudioInputStream entrada = AudioSystem.getAudioInputStream(new File(ruta))) {
            Clip clip = AudioSystem.getClip();
            clip.open(entrada);
            return clip;
        } catch (IOException
                | UnsupportedAudioFileException
                | LineUnavailableException
                | IllegalArgumentException e) {
            System.err.println("Sin sonido para " + ruta + ": " + e.getMessage());
            return null;
        }
    }

    /** Ganancia en decibeles, recortada a lo que permita la placa de sonido. */
    private static void aplicarGanancia(Clip clip, float db) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        FloatControl ganancia = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        ganancia.setValue(Math.max(ganancia.getMinimum(), Math.min(ganancia.getMaximum(), db)));
    }
}
