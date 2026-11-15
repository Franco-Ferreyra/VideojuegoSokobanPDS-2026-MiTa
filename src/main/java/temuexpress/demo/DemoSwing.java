package temuexpress.demo;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import temuexpress.controlador.ControladorJuego;
import temuexpress.modelo.creacion.ConstructorNivelSokoban;
import temuexpress.modelo.creacion.LectorNivel;
import temuexpress.modelo.dto.EventoPartidaDTO;
import temuexpress.modelo.dto.ResumenNivelDTO;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.vista.HojaSprites;
import temuexpress.vista.OyenteVista;
import temuexpress.vista.VentanaJuego;

/**
 * DEMO TEMPORAL: conecta la vista Swing con el motor real (LectorNivel y ControladorJuego) para ver
 * el juego en la ventana. No es parte del diseño: se borra cuando ControladorJuego maneje la vista,
 * el puntaje (Decorator) y las fábricas regionales.
 *
 * <p>Ejecutar desde la raíz del repo (para encontrar assets/img): java -cp target/classes
 * temuexpress.demo.DemoSwing [archivo de nivel]
 */
public final class DemoSwing implements OyenteVista {

    private static final String NIVEL_POR_DEFECTO = "src/test/resources/niveles/tutorial.txt";

    private enum Paso {
        INICIO,
        MENSAJE,
        JUEGO,
        RESUMEN,
        FINAL
    }

    private final VentanaJuego ventana = new VentanaJuego();
    private final Nivel nivel;
    private final ControladorJuego controlador;
    private Paso paso = Paso.INICIO;
    private int puntajeTotal;

    private DemoSwing(Nivel nivel) {
        this.nivel = nivel;
        this.controlador = new ControladorJuego(nivel);
    }

    public static void main(String[] args) throws IOException {
        Path archivo = Path.of(args.length > 0 ? args[0] : NIVEL_POR_DEFECTO);
        Path nombre = archivo.getFileName();
        String numero =
                nombre == null
                        ? "1-1"
                        : nombre.toString().replaceFirst("^nivel_", "").replaceFirst("\\.txt$", "");
        Nivel nivel = new LectorNivel(new ConstructorNivelSokoban()).construir(archivo, numero);
        SwingUtilities.invokeLater(() -> new DemoSwing(nivel).arrancar());
    }

    private void arrancar() {
        ventana.setOyente(this);
        ventana.setSprites(cargarSprites(carpetaRegion(), repartidor()));
        ventana.mostrarInicio();
        ventana.mostrar();
    }

    @Override
    public void alContinuar() {
        switch (paso) {
            case INICIO -> {
                paso = Paso.MENSAJE;
                ventana.mostrarMensaje(
                        nivel.getRegion(),
                        new String[] {
                            "Llevá cada paquete a su destino.",
                            "Flechas para moverte, UNDO para deshacer.",
                            "Nivel de prueba del motor."
                        });
            }
            case MENSAJE -> {
                paso = Paso.JUEGO;
                ventana.mostrarJuego(controlador.getTableroDTO());
                ventana.actualizar(controlador.getTableroDTO(), evento("MOVIMIENTO"));
            }
            case RESUMEN -> {
                paso = Paso.FINAL;
                ventana.mostrarFinal(
                        new String[] {
                            "TeMU Express cumplió: el paquete llegó.",
                            "Gracias por elegir TeMU Express."
                        });
            }
            case FINAL -> {
                paso = Paso.INICIO;
                puntajeTotal = 0;
                controlador.reiniciarNivel();
                ventana.setPuntajeTotal(0);
                ventana.mostrarInicio();
            }
            default -> {
                // en el juego, ENTER no hace nada
            }
        }
    }

    @Override
    public void alPresionarFlecha(String direccion) {
        if (paso != Paso.JUEGO || !controlador.mover(Direccion.valueOf(direccion))) {
            return;
        }
        if (controlador.esVictoria()) {
            ventana.actualizar(controlador.getTableroDTO(), evento("VICTORIA"));
            int puntaje = puntajeProvisorio();
            puntajeTotal += puntaje;
            ventana.setPuntajeTotal(puntajeTotal);
            ventana.mostrarResumen(
                    new ResumenNivelDTO(
                            controlador.getMovimientos(),
                            controlador.getEmpujes(),
                            controlador.getUsosUndo(),
                            nivel.getPar()),
                    puntaje);
            paso = Paso.RESUMEN;
        } else {
            ventana.actualizar(controlador.getTableroDTO(), evento("MOVIMIENTO"));
        }
    }

    @Override
    public void alPresionarUndo() {
        if (paso == Paso.JUEGO && controlador.deshacer()) {
            ventana.actualizar(controlador.getTableroDTO(), evento("UNDO"));
        }
    }

    @Override
    public void alPresionarReiniciar() {
        if (paso == Paso.JUEGO) {
            controlador.reiniciarNivel();
            ventana.actualizar(controlador.getTableroDTO(), evento("REINICIO"));
        }
    }

    /** Fórmula del punto 4 de Decisiones, sin recargo, hasta que exista el Decorator. */
    private int puntajeProvisorio() {
        int movimientos = controlador.getMovimientos();
        int usosUndo = controlador.getUsosUndo();
        int puntaje = 1000 - 3 * movimientos - 5 * controlador.getEmpujes() - 100 * usosUndo;
        puntaje += usosUndo == 0 ? 200 : 0;
        puntaje += movimientos <= nivel.getPar() ? 150 : 0;
        return Math.max(0, puntaje);
    }

    private EventoPartidaDTO evento(String tipo) {
        return new EventoPartidaDTO(
                tipo,
                nivel.getNumero(),
                controlador.getMovimientos(),
                controlador.getEmpujes(),
                controlador.getUsosUndoSeguidos());
    }

    /** Hace de fábrica regional: carpeta de assets/img según REGION y VARIANTE del TXT. */
    private String carpetaRegion() {
        switch (nivel.getVariante().toUpperCase(Locale.ROOT)) {
            case "ARG-CHILE":
                return "arg_chile";
            case "ECU-COL":
                return "ecuador_colombia";
            case "PERU-VEN":
                return "peru_venezuela";
            default:
                break;
        }
        switch (nivel.getRegion().toUpperCase(Locale.ROOT)) {
            case "EEUU":
                return "usa";
            case "ALEMANIA":
                return "alemania";
            default:
                return "china";
        }
    }

    private String repartidor() {
        switch (nivel.getRegion().toUpperCase(Locale.ROOT)) {
            case "EEUU":
                return "repartidor_amarillo";
            case "ALEMANIA":
                return "repartidor_rojo";
            case "SUDAMERICA":
                return "repartidor_verde";
            default:
                return "repartidor_azul";
        }
    }

    private static HojaSprites cargarSprites(String region, String repartidor) {
        HojaSprites s = new HojaSprites();
        String[] tiles = {
            "fondo",
            "pared",
            "piso",
            "destino",
            "muro_cerrado",
            "muro_abierto",
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
            "cinta_derecha"
        };
        for (String t : tiles) {
            s.poner(t, leer("assets/img/" + region + "/" + t + ".png"));
        }
        for (int n = 1; n <= 9; n++) {
            s.poner("portal_" + n, leer("assets/img/" + region + "/portal_" + n + ".png"));
        }
        s.poner("logo", leer("assets/img/logo.png"));
        for (String d : new String[] {"arriba", "abajo", "izquierda", "derecha", "esqueleto"}) {
            String clave = d.equals("esqueleto") ? d : "jugador_" + d;
            s.poner(clave, leer("assets/img/personaje/" + repartidor + "/" + d + ".png"));
        }
        return s;
    }

    private static Image leer(String ruta) {
        try {
            return ImageIO.read(new File(ruta));
        } catch (IOException e) {
            System.err.println("No se encontró " + ruta);
            return null;
        }
    }
}
