package temuexpress.modelo.nucleo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import temuexpress.modelo.creacion.ConstructorNivelSokoban;

class IntegridadModeloTest {
    @Test
    void colocarCajaNoPermiteSobrescribirOtraNiOcuparPared() {
        Celda celda = new Celda(new Piso());
        Caja original = new CajaNormal();
        celda.setCaja(original);
        assertThrows(IllegalStateException.class, () -> celda.setCaja(new CajaNormal()));
        assertSame(original, celda.getCaja());
        assertThrows(
                IllegalStateException.class,
                () -> new Celda(new Pared()).setCaja(new CajaNormal()));
        assertThrows(NullPointerException.class, () -> celda.setCaja(null));
        assertSame(original, celda.getCaja());
    }

    @Test
    void constructorDelTableroAislaLasCeldasYLasCajasDeOrigen() {
        Celda[][] origen = celdas();
        origen[1][2].setCaja(new CajaNormal());
        Tablero tablero = new Tablero(origen, new Posicion(1, 1));
        assertNotSame(origen[1][2].getCaja(), tablero.getCelda(new Posicion(1, 2)).getCaja());
        origen[1][2].retirarCaja();
        origen[1][1] = new Celda(new Pared());
        assertEquals(1, tablero.getPosicionesCajas().size());
        assertTrue(tablero.getCelda(new Posicion(1, 1)).getTerreno().esTransitablePorJugador());
        tablero.copiar().getCelda(new Posicion(1, 2)).retirarCaja();
        assertEquals(1, tablero.getPosicionesCajas().size());
    }

    @Test
    void jugadorNoPuedeSerMovidoPorElNucleoAParedCajaOFueraDelTablero() {
        Celda[][] origen = celdas();
        origen[1][2].setCaja(new CajaNormal());
        Posicion inicial = new Posicion(1, 1);
        Tablero tablero = new Tablero(origen, inicial);
        assertThrows(
                IllegalArgumentException.class,
                () -> tablero.setPosicionJugador(new Posicion(0, 0)));
        assertThrows(
                IllegalArgumentException.class,
                () -> tablero.setPosicionJugador(new Posicion(1, 2)));
        assertThrows(
                IllegalArgumentException.class,
                () -> tablero.setPosicionJugador(new Posicion(-1, 0)));
        assertEquals(inicial, tablero.getPosicionJugador());
    }

    @Test
    void tableroRechazaEstructuraVaciaIrregularOIncompleta() {
        Posicion posicion = new Posicion(0, 0);
        assertThrows(IllegalArgumentException.class, () -> new Tablero(new Celda[0][], posicion));
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new Tablero(
                                new Celda[][] {
                                    {new Celda(new Piso())},
                                    {new Celda(new Piso()), new Celda(new Piso())}
                                },
                                posicion));
        assertThrows(NullPointerException.class, () -> new Tablero(new Celda[1][1], posicion));
    }

    @Test
    void builderDaErroresClarosSiSeUsaFueraDeOrden() {
        ConstructorNivelSokoban constructor = new ConstructorNivelSokoban();
        Posicion posicion = new Posicion(1, 1);
        assertThrows(IllegalStateException.class, () -> constructor.colocarJugador(posicion));
        constructor.construirTablero(4, 3);
        assertThrows(IllegalStateException.class, () -> constructor.colocarJugador(posicion));
        assertThrows(
                IllegalArgumentException.class,
                () -> constructor.agregarTerreno(new Posicion(7, 7), ' '));
        constructor.agregarTerreno(posicion, ' ');
        assertThrows(IllegalStateException.class, () -> constructor.agregarTerreno(posicion, '#'));
        constructor.reiniciar();
        assertThrows(IllegalStateException.class, () -> constructor.colocarJugador(posicion));
    }

    private Celda[][] celdas() {
        return new Celda[][] {
            {
                new Celda(new Pared()),
                new Celda(new Pared()),
                new Celda(new Pared()),
                new Celda(new Pared())
            },
            {
                new Celda(new Pared()),
                new Celda(new Piso()),
                new Celda(new Piso()),
                new Celda(new Pared())
            },
            {
                new Celda(new Pared()),
                new Celda(new Pared()),
                new Celda(new Pared()),
                new Celda(new Pared())
            }
        };
    }
}
