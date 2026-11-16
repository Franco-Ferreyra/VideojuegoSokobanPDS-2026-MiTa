package temuexpress.modelo.nucleo;

/** Muro de la aduana: solo se puede atravesar mientras está abierto. Lo abre y cierra Partida. */
public final class Muro implements Terreno {
    private boolean abierto;

    public void abrir() {
        abierto = true;
    }

    public void cerrar() {
        abierto = false;
    }

    public boolean estaAbierto() {
        return abierto;
    }

    public boolean esTransitablePorJugador() {
        return abierto;
    }

    /** En el DTO: 'W' cerrado y 'w' abierto. */
    public char getSimbolo() {
        return abierto ? 'w' : 'W';
    }

    /** Tiene estado, así que cada tablero necesita su propio muro. */
    @Override
    public Terreno copiar() {
        Muro copia = new Muro();
        copia.abierto = abierto;
        return copia;
    }
}
