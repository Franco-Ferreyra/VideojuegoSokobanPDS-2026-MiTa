package temuexpress.modelo.creacion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import temuexpress.modelo.nucleo.Nivel;
import temuexpress.modelo.nucleo.Posicion;

/** Director del Builder. Las filas de las capas se preservan literalmente. */
public final class LectorNivel {
    private static final String MARCA_TERRENO = "[TERRENO]";
    private static final String MARCA_OBJETOS = "[OBJETOS]";
    private static final Set<String> CLAVES_ENCABEZADO = Set.of("REGION", "VARIANTE", "PAR");
    private ConstructorNivel constructor;

    public LectorNivel(ConstructorNivel constructor) {
        cambiarConstructor(constructor);
    }

    public void cambiarConstructor(ConstructorNivel constructor) {
        this.constructor = Objects.requireNonNull(constructor);
    }

    public Nivel construir(String archivo, String numero) throws IOException {
        return construir(Path.of(archivo), numero);
    }

    public Nivel construir(Path archivo, String numero) throws IOException {
        List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
        quitarBom(lineas);
        int inicioTerreno = lineas.indexOf(MARCA_TERRENO);
        int inicioObjetos = lineas.indexOf(MARCA_OBJETOS);
        validarMarcadores(lineas, inicioTerreno, inicioObjetos);
        Map<String, String> encabezado = leerEncabezado(lineas.subList(0, inicioTerreno));
        List<String> terreno = lineas.subList(inicioTerreno + 1, inicioObjetos);
        List<String> objetos = lineas.subList(inicioObjetos + 1, lineas.size());
        validarCapas(terreno, objetos);

        constructor.reiniciar();
        constructor.configurarNivel(
                numero, encabezado.get("REGION"), encabezado.get("VARIANTE"), leerPar(encabezado));
        constructor.construirTablero(terreno.getFirst().length(), terreno.size());
        construirTerreno(terreno);
        construirObjetos(objetos);
        return constructor.obtenerResultado();
    }

    private static void quitarBom(List<String> lineas) {
        if (!lineas.isEmpty() && lineas.getFirst().startsWith("\uFEFF")) {
            lineas.set(0, lineas.getFirst().substring(1));
        }
    }

    private static void validarMarcadores(List<String> lineas, int terreno, int objetos) {
        if (terreno < 0
                || objetos <= terreno + 1
                || objetos == lineas.size() - 1
                || Collections.frequency(lineas, MARCA_TERRENO) != 1
                || Collections.frequency(lineas, MARCA_OBJETOS) != 1) {
            throw new IllegalArgumentException(
                    "Se requieren [TERRENO] y [OBJETOS] una sola vez, en ese orden, con filas");
        }
    }

    private static Map<String, String> leerEncabezado(List<String> lineas) {
        Map<String, String> encabezado = new HashMap<>();
        for (int indice = 0; indice < lineas.size(); indice++) {
            String linea = lineas.get(indice).strip();
            if (linea.isEmpty()) continue;
            String[] partes = linea.split("=", -1);
            if (partes.length != 2 || !CLAVES_ENCABEZADO.contains(partes[0].strip())) {
                throw new IllegalArgumentException("Encabezado invalido en linea " + (indice + 1));
            }
            String clave = partes[0].strip();
            String valor = partes[1].strip();
            if (valor.isEmpty() || encabezado.putIfAbsent(clave, valor) != null) {
                throw new IllegalArgumentException("Encabezado vacio o duplicado: " + clave);
            }
        }
        if (!encabezado.keySet().equals(CLAVES_ENCABEZADO)) {
            throw new IllegalArgumentException("Faltan REGION, VARIANTE o PAR");
        }
        return encabezado;
    }

    private static int leerPar(Map<String, String> encabezado) {
        try {
            return Integer.parseInt(encabezado.get("PAR"));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("PAR debe ser un entero no negativo", error);
        }
    }

    private static void validarCapas(List<String> terreno, List<String> objetos) {
        if (objetos.size() != terreno.size()) {
            throw new IllegalArgumentException("Las capas deben tener la misma cantidad de filas");
        }
        int ancho = terreno.getFirst().length();
        for (int fila = 0; fila < terreno.size(); fila++) {
            if (terreno.get(fila).length() != ancho || objetos.get(fila).length() != ancho) {
                throw new IllegalArgumentException(
                        "Capas no rectangulares o de distinto ancho en fila " + (fila + 1));
            }
        }
    }

    private void construirTerreno(List<String> terreno) {
        for (int fila = 0; fila < terreno.size(); fila++) {
            for (int columna = 0; columna < terreno.get(fila).length(); columna++) {
                constructor.agregarTerreno(
                        new Posicion(fila, columna), terreno.get(fila).charAt(columna));
            }
        }
    }

    private void construirObjetos(List<String> objetos) {
        for (int fila = 0; fila < objetos.size(); fila++) {
            for (int columna = 0; columna < objetos.get(fila).length(); columna++) {
                Posicion posicion = new Posicion(fila, columna);
                char simbolo = objetos.get(fila).charAt(columna);
                switch (simbolo) {
                    case ' ' -> {}
                    case '@' -> constructor.colocarJugador(posicion);
                    case '$' -> constructor.agregarCaja(posicion, new CreadorCajaNormal());
                    case 'F', 'K' ->
                            throw new IllegalArgumentException(
                                    "Tipo de caja pendiente de implementar: " + simbolo);
                    default ->
                            throw new IllegalArgumentException(
                                    "Simbolo de objeto desconocido en fila "
                                            + (fila + 1)
                                            + ", columna "
                                            + (columna + 1));
                }
            }
        }
    }
}
