package temuexpress.modelo.nucleo;

public record Posicion(int fila, int columna) {
    public Posicion siguiente(Direccion dir) {
        return new Posicion(fila + dir.getFila(), columna + dir.getColumna());
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
