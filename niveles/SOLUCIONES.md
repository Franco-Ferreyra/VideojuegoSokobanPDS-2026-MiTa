# Soluciones de los niveles

Cada mapa se comprobó con un programa que busca la solución y la repite paso a paso hasta la victoria, con las reglas del punto 2 de `Decisiones_MVP_TeMU_ExpressFinal.txt` (detalle abajo). Este archivo lo pide `docs/FORMATO_NIVELES.md`.

- **Secuencia:** A arriba, B abajo, I izquierda, D derecha (las mismas letras que la consola del motor). Va en grupos de 10 para leerla mejor.
- **PAR:** movimientos de la solución más un 20 % de margen, redondeado hacia arriba.
- En 1-1 a 4-2 la solución es la más corta en movimientos. En 4-3 es una solución comprobada; puede existir una más corta.

| Nivel | Región | Movimientos | Empujes | PAR |
|---|---|---|---|---|
| 1-1 | China | 28 | 8 | 34 |
| 2-1 | Estados Unidos | 21 | 8 | 26 |
| 3-1 | Alemania | 38 | 21 | 46 |
| 4-1 | Argentina - Chile | 58 | 22 | 70 |
| 4-2 | Ecuador - Colombia | 51 | 22 | 62 |
| 4-3 | Perú - Venezuela | 79 | 29 | 95 |

## 1-1 China

Cajas normales en la zona de la izquierda (tutorial). La caja llave en el cerrojo abre el muro y da paso a la zona de la derecha.

```text
#############
#     #     #
# $ . W  .  #
#  .$ #  $  #
# K L #  $  #
#  @  #   . #
#############
```

- Movimientos: 28 · Empujes: 8 · PAR: 34

```text
IIADDAIIAD DADDBBIDAD DBBDDADB
```

## 2-1 Estados Unidos

Caja frágil: necesita exactamente 3 empujes para llegar a su destino; un empuje de más la rompe. Caja llave, cerrojo y muro.

```text
#############
#    #      #
# K  W F  . #
#    #  $   #
# L  #    . #
#  @ ####   #
#############
```

- Movimientos: 21 · Empujes: 8 · PAR: 26

```text
AAAAIBBADD DDDDDIBIBD D
```

## 3-1 Alemania

Dos agujeros cortan el paso: hay que tirar una caja en cada uno para cruzar. Caja frágil del otro lado.

```text
#############
#    #      #
# $  O  .   #
#@$$ #  F . #
# $  O  .   #
#    #      #
#############
```

- Movimientos: 38 · Empujes: 21 · PAR: 46

```text
ADDDIIIBBD DDIIAIADDD DDDIIIIBIB DDDDDADD
```

## 4-1 Argentina - Chile

Hielo: las cajas patinan hasta los muros. La caja llave abre los dos muros. Hay un agujero que se tapa con una caja.

```text
#############
#     ####  #
# $ ~~~~~W .#
#@ K $  L# .#
# $ ~~~~~W .#
# $ O    #  #
#############
```

- Movimientos: 58 · Empujes: 22 · PAR: 70

```text
BBDDIIADDI AIADDBIBDA DDBBDDAIAD ADDDADBIII IBBDDDIIIA IIADDDDD
```

## 4-2 Ecuador - Colombia

Cinta hacia la derecha que cruza la pared: lleva las cajas al otro lado (un deslizamiento cuenta como un solo empuje). Caja frágil, caja llave, cerrojo y muro.

```text
#############
#     #  .  #
#   F>>>>   #
# $ $ #     #
# K L W  .  #
#@    #  .  #
#############
```

- Movimientos: 51 · Empujes: 22 · PAR: 62

```text
ADABDAADAI IIBDDDIBDI BBDDAABDDA DAIADDBIDB DDAAIBBBDB I
```

## 4-3 Perú - Venezuela

Nivel final: portal (par 1), hielo que lleva la caja al portal, cinta que la baja al destino, agujero, caja frágil, caja llave, cerrojo y muro.

```text
#############
# $ ~~1#    #
#@     W  1v#
# K F O#   v#
#  $ $ #   .#
# L    #  ..#
#############
```

- Movimientos: 79 · Empujes: 29 · PAR: 95

```text
ADBDBDBBIA DDIIABIIAA DDDIIBBAAD DDDDDADBBB IBDDAAIAII IIIIIADBBD DBBDAAIADD DADBBBIBD
```

## Reglas con las que se verificaron

Son las del punto 2 de Decisiones. Donde Decisiones no detalla un caso, se usó esta interpretación; si el motor la implementa distinto, hay que volver a comprobar los mapas.

- El jugador camina sobre piso, destino, cerrojo, hielo, agujero tapado y muro abierto. No pisa agujeros abiertos, cintas ni portales.
- Una caja entra a cualquier casilla salvo pared, muro cerrado u otra caja.
- **Hielo:** la caja sigue en la dirección del empuje mientras la casilla siguiente le permita entrar. Si la siguiente no es hielo, entra y aplica el efecto de esa casilla (por ejemplo, cae en un agujero). Si está bloqueada, la caja se queda sobre el hielo.
- **Cinta:** la caja sigue en la dirección de la cinta. Al salir aplica el efecto de la casilla nueva. Si la salida está bloqueada, la caja se queda sobre la cinta.
- **Portal:** la caja aparece en el portal del mismo número y sale una casilla en la dirección en la que venía. Si esa salida está bloqueada, el empuje no se hace.
- **Agujero:** la caja normal o frágil que cae desaparece y el agujero pasa a ser piso. Si cae la caja llave, el nivel no se puede ganar.
- **Caja frágil:** cada empuje resta 1 a la resistencia (empieza en 4); un deslizamiento cuenta como un empuje. Con el cuarto empuje se rompe.
- **Muros:** están abiertos mientras todos los cerrojos tienen una caja llave encima. Para la caja empujada se mira el estado sin contar esa misma caja (si la llave sale del cerrojo, los muros ya están cerrados para ella).
- **Victoria:** cajas normales y frágiles sobre destinos, y cajas llave sobre cerrojos.
