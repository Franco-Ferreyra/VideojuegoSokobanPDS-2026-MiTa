package temuexpress.modelo.dto;

public record TableroDTO(int ancho, int alto, String terreno, String objetos) {
    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public String getTerreno() {
        return terreno;
    }

    public String getObjetos() {
        return objetos;
    }
}
