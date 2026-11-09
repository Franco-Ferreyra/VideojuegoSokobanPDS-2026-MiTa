package temuexpress.modelo.nucleo;

public interface Caja {
    boolean estaEntregada(Celda celda);

    void alSerEmpujada();

    boolean estaRota();

    TipoCaja getTipo();
}
