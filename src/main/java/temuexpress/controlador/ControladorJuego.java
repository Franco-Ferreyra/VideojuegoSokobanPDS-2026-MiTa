package temuexpress.controlador;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;
import temuexpress.modelo.dto.EventoPartidaDTO;
import temuexpress.modelo.dto.ResumenNivelDTO;
import temuexpress.modelo.dto.TableroDTO;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.HistorialPartida;
import temuexpress.modelo.nucleo.MementoPartida;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.modelo.nucleo.Partida;
import temuexpress.modelo.puntaje.BonusPorPar;
import temuexpress.modelo.puntaje.BonusSinUndo;
import temuexpress.modelo.puntaje.CalculadorPuntaje;
import temuexpress.modelo.puntaje.PuntajeBase;
import temuexpress.modelo.puntaje.RecargoUndo;
import temuexpress.recursos.GestorSonido;
import temuexpress.vista.OyenteVista;
import temuexpress.vista.VentanaJuego;

/**
 * Controlador del MVC (Clase 6): recibe lo que hace el jugador en la vista, se lo pide al modelo y
 * maneja el flujo de los 6 niveles (inicio, mensaje, juego, resumen y final). Undo y Reiniciar
 * siguen el flujo del Memento con Partida e HistorialPartida.
 *
 * <p>Con el constructor de un Nivel funciona sin ventana (consola y pruebas).
 */
public final class ControladorJuego implements OyenteVista {
    private static final List<String> NIVELES = List.of("1-1", "2-1", "3-1", "4-1", "4-2", "4-3");

    private enum Paso {
        INICIO,
        MENSAJE,
        JUEGO,
        RESUMEN,
        FINAL
    }

    private final HistorialPartida historial = new HistorialPartida();
    private final VentanaJuego ventana;
    private final PreparadorNivelFacade preparador;
    private Partida partida;
    private Nivel nivel;
    private Paso paso = Paso.INICIO;
    private int indiceNivel;
    private int puntajeTotal;
    private int pagina;
    private String carpetaVoces = "";

    /** Sin ventana: consola y pruebas. */
    public ControladorJuego(Nivel nivel) {
        this.ventana = null;
        this.preparador = null;
        iniciarNivel(nivel);
    }

    /** Juego completo: crea la ventana Swing. Llamar desde el hilo de Swing. */
    public ControladorJuego(PreparadorNivelFacade preparador) {
        this.ventana = new VentanaJuego();
        this.preparador = Objects.requireNonNull(preparador);
    }

    public void iniciar() {
        ventana.setOyente(this);
        volverAlInicio();
        ventana.mostrar();
    }

    /** Portada con el nivel 1-1 cargado y el HUD y el 1UP en cero. */
    private void volverAlInicio() {
        cargarNivel(0);
        puntajeTotal = 0;
        ventana.setPuntajeTotal(0);
        ventana.actualizar(
                getTableroDTO(), new EventoPartidaDTO("INICIO", nivel.getNumero(), 0, 0, 0));
        paso = Paso.INICIO;
        ventana.mostrarInicio();
    }

    // ---- Acciones sobre la partida (Memento) ----

    public void iniciarNivel(Nivel nivel) {
        this.nivel = Objects.requireNonNull(nivel);
        partida = new Partida(nivel);
        historial.limpiar();
    }

    public boolean mover(Direccion dir) {
        MementoPartida anterior = partida.crearMemento();
        if (!partida.mover(dir)) return false;
        historial.guardar(anterior);
        return true;
    }

    public boolean deshacer() {
        if (!partida.puedeUsarUndo()) return false;
        MementoPartida anterior = historial.deshacer();
        if (anterior == null) return false;
        partida.restaurar(anterior);
        partida.registrarUsoUndo();
        return true;
    }

    public void reiniciarNivel() {
        partida.reiniciar();
        historial.limpiar();
    }

    public TableroDTO getTableroDTO() {
        return partida.getTableroDTO();
    }

    public boolean esVictoria() {
        return partida.esVictoria();
    }

    public boolean esDerrota() {
        return partida.esDerrota();
    }

    public int getMovimientos() {
        return partida.getMovimientos();
    }

    public int getEmpujes() {
        return partida.getEmpujes();
    }

    public int getUsosUndo() {
        return partida.getUsosUndo();
    }

    public int getUsosUndoRestantes() {
        return partida.getUsosUndoRestantes();
    }

    public int getUsosUndoSeguidos() {
        return partida.getUsosUndoSeguidos();
    }

    // ---- Lo que avisa la vista ----

    @Override
    public void alPresionarFlecha(String direccion) {
        if (paso != Paso.JUEGO || !mover(Direccion.valueOf(direccion))) return;
        if (esVictoria()) terminarNivel();
    }

    @Override
    public void alPresionarUndo() {
        if (paso == Paso.JUEGO) deshacer();
    }

    @Override
    public void alPresionarReiniciar() {
        if (paso == Paso.JUEGO) reiniciarNivel();
    }

    @Override
    public void alContinuar() {
        switch (paso) {
            case INICIO -> mostrarMensaje();
            case MENSAJE -> {
                if (pagina + 1 < MensajesRegionales.paginas(nivel.getNumero()).size()) {
                    pagina++;
                    mostrarPagina();
                    return;
                }
                paso = Paso.JUEGO;
                // La voz del mensaje de inicio no sigue sonando durante el juego.
                GestorSonido.getInstancia().detenerVoz();
                ventana.mostrarJuego(getTableroDTO());
                ventana.actualizar(
                        getTableroDTO(),
                        new EventoPartidaDTO("INICIO", nivel.getNumero(), 0, 0, 0));
            }
            case RESUMEN -> {
                if (indiceNivel + 1 < NIVELES.size()) {
                    cargarNivel(indiceNivel + 1);
                    mostrarMensaje();
                } else {
                    // El cierre tiene 2 páginas: qué fue de cada repartidor y el puntaje.
                    paso = Paso.FINAL;
                    pagina = 0;
                    MensajesRegionales.Pagina cierre = MensajesRegionales.cierreRepartidores();
                    ventana.mostrarMensaje(cierre.titulo(), cierre.lineas(), "ENTER para seguir");
                }
            }
            case FINAL -> {
                if (pagina == 0) {
                    pagina = 1;
                    ventana.mostrarFinal(MensajesRegionales.finalDelJuego(puntajeTotal));
                    return;
                }
                volverAlInicio();
            }
            default -> {
                // En el juego, ENTER no hace nada.
            }
        }
    }

    // ---- Flujo de niveles ----

    /**
     * La fachada lee el nivel, elige la fábrica regional y pone la música; después da los sprites,
     * el fondo animado y la carpeta de voces de la región. El controlador solo habla con ella.
     */
    private void cargarNivel(int indice) {
        indiceNivel = indice;
        try {
            iniciarNivel(preparador.prepararNivel(NIVELES.get(indice)));
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el nivel " + NIVELES.get(indice), e);
        }
        carpetaVoces = preparador.getCarpetaVoces();
        ventana.setSprites(preparador.armarSprites());
        ventana.setAnimacionFondo(preparador.getAnimacionFondo());
        partida.suscribir(new PresentadorPartida(ventana, this::getTableroDTO, nivel.getNumero()));
        ReaccionAudioPartida audio = new ReaccionAudioPartida(carpetaVoces);
        audio.precargar();
        partida.suscribir(audio);
    }

    private void mostrarMensaje() {
        paso = Paso.MENSAJE;
        pagina = 0;
        mostrarPagina();
        GestorSonido.getInstancia()
                .reproducirVoz(carpetaVoces + "inicio.wav", ReaccionAudioPartida.PRIORIDAD_MAXIMA);
    }

    /** El mensaje de inicio puede tener varias páginas: la historia del repartidor y la misión. */
    private void mostrarPagina() {
        List<MensajesRegionales.Pagina> paginas = MensajesRegionales.paginas(nivel.getNumero());
        MensajesRegionales.Pagina actual = paginas.get(pagina);
        boolean ultima = pagina == paginas.size() - 1;
        ventana.mostrarMensaje(
                actual.titulo(),
                actual.lineas(),
                ultima ? "ENTER para empezar" : "ENTER para seguir");
    }

    private void terminarNivel() {
        ResumenNivelDTO resumen =
                new ResumenNivelDTO(getMovimientos(), getEmpujes(), getUsosUndo(), nivel.getPar());
        int puntaje = Math.max(0, calculadorPara(nivel).calcular(resumen));
        puntajeTotal += puntaje;
        ventana.setPuntajeTotal(puntajeTotal);
        ventana.mostrarResumen(resumen, puntaje);
        paso = Paso.RESUMEN;
    }

    /** Decorator: base + bonus sin Undo + bonus por PAR, y el recargo de Undo solo en 4-1. */
    private static CalculadorPuntaje calculadorPara(Nivel nivel) {
        CalculadorPuntaje calculador = new BonusPorPar(new BonusSinUndo(new PuntajeBase()));
        if ("4-1".equals(nivel.getNumero())) calculador = new RecargoUndo(calculador);
        return calculador;
    }
}
