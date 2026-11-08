package temuexpress.modelo.nucleo;

import java.util.Objects;

public final class Nivel {
    private final String numero, region, variante;
    private final int par;
    private final Tablero tablero;

    public Nivel(String numero, String region, String variante, int par, Tablero tablero) {
        this.numero = Objects.requireNonNull(numero);
        this.region = Objects.requireNonNull(region);
        this.variante = Objects.requireNonNull(variante);
        this.par = par;
        this.tablero = tablero.copiar();
    }

    public String getNumero() {
        return numero;
    }

    public String getRegion() {
        return region;
    }

    public String getVariante() {
        return variante;
    }

    public int getPar() {
        return par;
    }

    public Tablero getTablero() {
        return tablero.copiar();
    }
}
