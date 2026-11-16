package temuexpress.recursos;

import java.util.ArrayList;
import java.util.List;

/** El escenario cambia con la variante (cada una tiene su fondo y su animación). */
public final class EscenarioSudamerica extends EscenarioCarpeta {
    /** Debajo de esta fila empiezan el tablero y los paneles del HUD. */
    private static final int FIN_DEL_FONDO = 186;

    EscenarioSudamerica(String carpeta) {
        super(carpeta);
    }

    @Override
    public AnimacionFondo getAnimacion() {
        return switch (carpeta()) {
            case "arg_chile" -> argentinaChile();
            case "ecuador_colombia" -> ecuadorColombia();
            default -> peruVenezuela();
        };
    }

    /** Atardecer, nubes, un avión con su estela y nieve. */
    private AnimacionFondo argentinaChile() {
        return new FondoAnimado(
                getFondo(),
                40,
                92,
                new CapaAtardecer(40, 92),
                new CapaNubes(carpeta(), 40, 92),
                new CapaVolador(
                        carpeta() + "/anim/avion.png",
                        t -> {
                            double p = CapaVolador.avance(t, 150);
                            return new double[] {-50 + p * 540, 54 - p * 8};
                        }),
                new CapaNieve(230, 37, FIN_DEL_FONDO));
    }

    /** Atardecer, nubes y una bandada de loros. */
    private AnimacionFondo ecuadorColombia() {
        String[] colores = {"verde", "rojo", "verde", "amarillo", "verde"};
        List<Capa> capas = new ArrayList<>();
        capas.add(new CapaAtardecer(40, 82));
        capas.add(new CapaNubes(carpeta(), 40, 82));
        for (int i = 0; i < colores.length; i++) {
            int n = i;
            String loro = carpeta() + "/anim/loro_" + colores[i];
            capas.add(
                    new CapaVolador(
                            List.of(Sprite.leer(loro + "_1.png"), Sprite.leer(loro + "_2.png")),
                            2,
                            t -> {
                                double p = CapaVolador.avance(t, 150);
                                return new double[] {
                                    -40 + p * 520 - n * 11,
                                    50 + (n % 2 == 0 ? 0 : 6) + 2 * Math.sin(p * 30 + n)
                                };
                            }));
        }
        return new FondoAnimado(getFondo(), 40, 82, capas.toArray(new Capa[0]));
    }

    /** Atardecer, nubes, el agua del Salto Ángel, los helicópteros y un cóndor. */
    private AnimacionFondo peruVenezuela() {
        String condor = carpeta() + "/anim/condor_";
        return new FondoAnimado(
                getFondo(),
                37,
                130,
                new CapaAtardecer(40, 90),
                new CapaCascada(286, 96, 310, 172),
                new CapaHelicopteros(carpeta()),
                new CapaNubes(carpeta(), 40, 90),
                new CapaVolador(
                        List.of(Sprite.leer(condor + "1.png"), Sprite.leer(condor + "2.png")),
                        8,
                        t -> {
                            double p = CapaVolador.avance(t, 240);
                            return new double[] {60 + p * 300, 56 + 8 * Math.sin(2 * Math.PI * p)};
                        }));
    }
}
