# Contrato de archivos de niveles — TeMU Express

Este contrato concreta la sintaxis del punto 8 de `Decisiones_MVP_TeMU_ExpressFinal.txt`, que sigue siendo la fuente de reglas. Encabezados con `=` acordados con el equipo. No modifica las decisiones del MVP.

## Ubicación y nombres

Los seis mapas del equipo van en `niveles/`, relativos a la raíz del repositorio. Un TXT por nivel:

| Archivo | REGION | VARIANTE |
| --- | --- | --- |
| `nivel_1-1.txt` | `CHINA` | `NINGUNA` |
| `nivel_2-1.txt` | `EEUU` | `NINGUNA` |
| `nivel_3-1.txt` | `ALEMANIA` | `NINGUNA` |
| `nivel_4-1.txt` | `SUDAMERICA` | `ARG-CHILE` |
| `nivel_4-2.txt` | `SUDAMERICA` | `ECU-COL` |
| `nivel_4-3.txt` | `SUDAMERICA` | `PERU-VEN` |

Los valores de esta tabla son la convención técnica del cargador: `SUDAMERICA` agrupa la familia regional y `VARIANTE` distingue sus tres mapas. `NINGUNA` es obligatorio para las otras regiones. Usar mayúsculas, sin tildes. El número no es un encabezado: se pasa por separado a `LectorNivel.construir(archivo, numero)`; la consola lo obtiene del nombre del archivo.

## Sintaxis

- UTF-8; acepta BOM inicial y saltos LF o CRLF.
- Exactamente un encabezado `REGION`, `VARIANTE` y `PAR`, antes de las capas. El orden de estos tres es libre. Se permiten espacios junto al `=` y líneas vacías entre encabezados.
- `PAR` es un entero no negativo. Para los mapas definitivos, proponer los movimientos de una solución comprobada más un margen chico y registrar la solución en `niveles/SOLUCIONES.md`.
- Marcadores exactos, en este orden: `[TERRENO]` y `[OBJETOS]`.
- Cada capa tiene las mismas dimensiones: de 3 a 13 columnas y de 3 a 7 filas, paredes incluidas. Cada fila tiene exactamente el mismo ancho.
- En las capas **cada espacio cuenta**, incluidos los iniciales, finales y las filas compuestas solo por espacios. No usar tabulaciones, comentarios, líneas separadoras ni quitar espacios al guardar. Una fila vacía no equivale a una fila de piso.
- Se permite el salto de línea final habitual; no agregar filas vacías extra al final.

### Ejemplo mínimo válido para el motor actual

```text
REGION=CHINA
VARIANTE=NINGUNA
PAR=4
[TERRENO]
#######
#    .#
#     #
#######
[OBJETOS]
       
  @$   
       
       
```

Las cuatro filas de OBJETOS tienen **7 caracteres cada una**. Las filas 1, 3 y 4 son siete espacios. La fila 2 es dos espacios, `@`, `$` y tres espacios. El archivo ejecutable de este ejemplo está en `src/test/resources/niveles/tutorial.txt`. Se resuelve con `DD`: 2 movimientos, 2 empujes; PAR=4 es un margen ilustrativo. Es un recurso de prueba, no reemplaza el nivel final 1-1, que lleva llave, cerrojo y muro.

## Símbolos del MVP

| Capa | Símbolo | Significado | Motor |
| --- | --- | --- | --- |
| TERRENO | `#` | Pared | Implementado |
| TERRENO | espacio | Piso | Implementado |
| TERRENO | `.` | Destino | Implementado |
| TERRENO | `L` | Cerrojo | Implementado |
| TERRENO | `W` | Muro inicialmente cerrado | Implementado |
| TERRENO | `O` | Agujero | Implementado |
| TERRENO | `~` | Hielo | Implementado |
| TERRENO | `>` `<` `^` `v` | Cinta y dirección | Implementado |
| TERRENO | `1` a `9` | Par de portales del mismo color | Implementado |
| OBJETOS | espacio | Sin objeto | Implementado |
| OBJETOS | `@` | Jugador | Implementado |
| OBJETOS | `$` | Caja normal | Implementado |
| OBJETOS | `F` | Caja frágil (resistencia 4) | Implementado |
| OBJETOS | `K` | Caja llave | Implementado |

Cualquier otro símbolo genera un error explícito; nunca se interpreta como piso o caja normal. Las reglas de cada mecánica están al final de `niveles/SOLUCIONES.md`.

## Validaciones

`LectorNivel` rechaza encabezados ausentes, duplicados o desconocidos, `PAR` que no sea un entero, marcadores faltantes o repetidos, capas de distinto tamaño o no rectangulares y símbolos desconocidos.

`ConstructorNivelSokoban` controla:
- `REGION` y `VARIANTE` válidas (tabla de arriba) y `PAR` no negativo.
- Dimensiones de 3 a 13 columnas y de 3 a 7 filas, y el mapa rodeado de paredes.
- Exactamente un jugador.
- El jugador y las cajas arrancan solo sobre piso, destino o cerrojo: nunca sobre otra caja, pared, muro (arranca cerrado), agujero, hielo, cinta ni portal.
- Al menos una caja normal o frágil, y normales + frágiles = destinos + agujeros.
- Cajas llave = cerrojos, y muros solo si hay al menos un cerrojo.
- Cada dígito de portal exactamente dos veces (un par).
- Que cintas, hielo y portales no formen un ciclo.

No demuestra que el nivel tenga solución: los seis mapas se verificaron aparte con un solucionador. Las dos capas evitan superponer dos terrenos en una misma casilla y permiten iniciar una caja o el jugador sobre un destino.

`niveles/SOLUCIONES.md` queda a cargo de mapas: secuencia A/B/I/D, movimientos, empujes y PAR por nivel. La distribución de mecánicas y la zona tutorial de China se rigen por los puntos 5 y 6 de Decisiones.
