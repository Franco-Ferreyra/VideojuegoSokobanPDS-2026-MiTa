package temuexpress.recursos;

import java.util.ArrayList;
import java.util.List;

public final class EscenarioChina extends EscenarioCarpeta {
    private static final int CIELO_DESDE = 40;
    private static final int CIELO_HASTA = 78;
    private static final int[] FAROLITOS_X = {70, 150, 290, 360};

    public EscenarioChina() {
        super("china");
    }

    /** Nubes blancas y farolitos rojos que suben flotando. */
    @Override
    public AnimacionFondo getAnimacion() {
        List<Sprite> farolito =
                List.of(
                        Sprite.leer("china/anim/farolito_1.png"),
                        Sprite.leer("china/anim/farolito_2.png"));
        List<Capa> capas = new ArrayList<>();
        capas.add(new CapaNubes(carpeta(), CIELO_DESDE, CIELO_HASTA));
        for (int i = 0; i < FAROLITOS_X.length; i++) {
            int n = i;
            capas.add(
                    new CapaVolador(
                            farolito,
                            4,
                            t -> {
                                double q = (CapaVolador.avance(t, 240) + n * 0.27) % 1;
                                return new double[] {
                                    FAROLITOS_X[n] + 3 * Math.sin(q * 4 * Math.PI),
                                    CIELO_HASTA - q * (CIELO_HASTA - CIELO_DESDE + 14)
                                };
                            }));
        }
        return new FondoAnimado(getFondo(), CIELO_DESDE, CIELO_HASTA, capas.toArray(new Capa[0]));
    }
}
