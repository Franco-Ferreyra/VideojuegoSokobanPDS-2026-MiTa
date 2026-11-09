package temuexpress;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;
import temuexpress.controlador.ControladorJuego;
import temuexpress.modelo.creacion.ConstructorNivelSokoban;
import temuexpress.modelo.creacion.LectorNivel;
import temuexpress.modelo.dto.TableroDTO;
import temuexpress.modelo.nucleo.Direccion;
import temuexpress.modelo.nucleo.Nivel;

/** Banco de prueba por consola hasta integrar Swing. */
public final class Aplicacion {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println(
                    "Uso: java -jar target/temu-express-0.1.0-SNAPSHOT.jar <archivo.txt>");
            return;
        }
        Path archivo = Path.of(args[0]);
        Nivel nivel =
                new LectorNivel(new ConstructorNivelSokoban())
                        .construir(
                                archivo,
                                Objects.requireNonNull(
                                                archivo.getFileName(),
                                                "Se requiere un archivo de nivel")
                                        .toString()
                                        .replaceFirst("^nivel_", "")
                                        .replaceFirst("\\.txt$", ""));
        ControladorJuego juego = new ControladorJuego(nivel);
        System.out.println("A/B/I/D: mover | U: Undo | R: reiniciar | S: salir");
        try (Scanner entrada = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                mostrar(juego);
                if (!entrada.hasNextLine()) break;
                String accion = entrada.nextLine().strip().toUpperCase(java.util.Locale.ROOT);
                switch (accion) {
                    case "A" -> juego.mover(Direccion.ARRIBA);
                    case "B" -> juego.mover(Direccion.ABAJO);
                    case "I" -> juego.mover(Direccion.IZQUIERDA);
                    case "D" -> juego.mover(Direccion.DERECHA);
                    case "U" -> juego.deshacer();
                    case "R" -> juego.reiniciarNivel();
                    case "S" -> {
                        return;
                    }
                    default -> System.out.println("Accion desconocida");
                }
            }
        }
    }

    private static void mostrar(ControladorJuego juego) {
        TableroDTO dto = juego.getTableroDTO();
        String[] t = dto.terreno().split("\n"), o = dto.objetos().split("\n");
        for (int f = 0; f < dto.alto(); f++) {
            StringBuilder fila = new StringBuilder();
            for (int c = 0; c < dto.ancho(); c++)
                fila.append(o[f].charAt(c) == ' ' ? t[f].charAt(c) : o[f].charAt(c));
            System.out.println(fila);
        }
        System.out.printf(
                "Movimientos: %d | Empujes: %d | Undo: %d | Restantes: %d%s%n",
                juego.getMovimientos(),
                juego.getEmpujes(),
                juego.getUsosUndo(),
                juego.getUsosUndoRestantes(),
                juego.esVictoria() ? " | VICTORIA" : "");
    }
}
