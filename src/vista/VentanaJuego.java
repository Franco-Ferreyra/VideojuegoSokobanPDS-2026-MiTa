package vista;

import dto.EventoPartidaDTO;
import dto.ResumenNivelDTO;
import dto.TableroDTO;

import javax.swing.JFrame;

/**
 * Ventana del juego. Es lo único de la vista que usa el controlador.
 * Solo recibe DTO y textos; no contiene reglas del juego (MVC, Clase 6).
 * Usa un JFrame en lugar de heredarlo, así el controlador solo ve estos métodos.
 * Llamar a estos métodos desde el hilo de Swing (SwingUtilities.invokeLater).
 */
public class VentanaJuego {

    private final JFrame marco = new JFrame("TeMU Express - 特穆快递");
    private final PanelJuego panel = new PanelJuego();

    public VentanaJuego() {
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.add(panel);
        marco.pack();
        marco.setLocationRelativeTo(null);
    }

    public void mostrar() {
        marco.setVisible(true);
        panel.requestFocusInWindow();
    }

    public void setOyente(OyenteVista oyente) {
        panel.setOyente(oyente);
    }

    /**
     * Imágenes de la región actual (las arma el controlador con la fábrica).
     * Incluye el fondo, que ya trae el nombre de la región y los botones.
     */
    public void setSprites(HojaSprites sprites) {
        panel.setSprites(sprites);
        panel.repaint();
    }

    /** 1UP: suma de los puntajes de los niveles terminados. */
    public void setPuntajeTotal(int puntajeTotal) {
        panel.hud().setPuntajeTotal(puntajeTotal);
        panel.repaint();
    }

    public void mostrarInicio() {
        mostrarPantalla(new PantallaInicio());
    }

    /** Mensaje regional al iniciar el nivel. */
    public void mostrarMensaje(String titulo, String[] lineas) {
        mostrarPantalla(new PantallaMensaje(titulo, lineas));
    }

    /** Pasa a la pantalla de juego con este tablero. */
    public void mostrarJuego(TableroDTO tablero) {
        panel.tablero().setTablero(tablero);
        mostrarPantalla(new PantallaJuego());
    }

    /** Redibuja el tablero y el HUD después de un cambio de la partida. */
    public void actualizar(TableroDTO tablero, EventoPartidaDTO evento) {
        panel.tablero().setTablero(tablero);
        panel.hud().actualizar(evento);
        panel.repaint();
    }

    public void mostrarResumen(ResumenNivelDTO resumen, int puntaje) {
        mostrarPantalla(new PantallaResumen(resumen, puntaje));
    }

    /** Derrota: esqueleto, mensaje y los botones UNDO y REINICIAR activos. */
    public void mostrarDerrota(String[] lineas) {
        mostrarPantalla(new PantallaDerrota(lineas));
    }

    public void mostrarFinal(String[] lineas) {
        mostrarPantalla(new PantallaFinal(lineas));
    }

    private void mostrarPantalla(Pantalla pantalla) {
        panel.setPantalla(pantalla);
        panel.repaint();
    }
}
