# Primer entregable del motor

## Compilar y probar

Requiere **JDK 21** (`java` y `javac` disponibles; `JAVA_HOME` puede apuntar al JDK). Maven Wrapper fija Maven 3.9.9; no hace falta instalar Maven. La primera ejecución necesita Internet para descargar Maven, plugins y JUnit.

PowerShell, desde la raíz del repo:

```powershell
.\mvnw.cmd clean verify
java -jar target/temu-express-0.1.0-SNAPSHOT.jar src/test/resources/niveles/tutorial.txt
```

En Linux/macOS: `sh ./mvnw clean verify` y el mismo comando `java -jar`.

La clase principal es `temuexpress.Aplicacion`. Es una consola para probar el motor antes de integrar Swing. Ingresar una acción por línea: A arriba, B abajo, I izquierda, D derecha, U Undo, R reiniciar, S salir. El tutorial se resuelve con dos D. Tras ganar se puede usar U o R; el motor bloquea movimientos nuevos hasta restaurar/reiniciar.

## Arquitectura y contratos

Los nombres se tomaron de `diagramas/TeMUExpress.mdj` y las reglas de `Decisiones_MVP_TeMU_ExpressFinal.txt`. El archivo PUML en la carpeta de propuestas antiguas no es el contrato vigente.

- `modelo.nucleo`: `Nivel`, `Tablero`, `Celda`, `Posicion`, `Direccion`, terrenos básicos, `CajaNormal`, `Partida`, `MementoPartida`, `EstadoCaja` e `HistorialPartida`.
- `modelo.creacion`: `LectorNivel` dirige `ConstructorNivel` / `ConstructorNivelSokoban` (Builder); `CreadorCaja` / `CreadorCajaNormal` implementan Factory Method.
- `modelo.estrategias`: contrato `EstrategiaDesplazamientoCaja` y `EstrategiaNormal`. El terreno de entrada elige la estrategia; esta calcula el destino sin modificar el tablero.
- `modelo.dto`: `TableroDTO` inmutable, con ancho, alto y cadenas de ambas capas separadas por `\n`.
- `controlador`: `ControladorJuego` coordina acciones e historial. No depende de Swing.

Ejemplo para conectar la futura vista:

```java
Nivel nivel = new LectorNivel(new ConstructorNivelSokoban())
    .construir("niveles/nivel_1-1.txt", "1-1");
ControladorJuego controlador = new ControladorJuego(nivel);
controlador.mover(Direccion.DERECHA); // true solamente si se confirmó
TableroDTO tablero = controlador.getTableroDTO();
controlador.deshacer();
controlador.reiniciarNivel();
```

Ese ejemplo requiere un nivel con las mecánicas implementadas; para ejecutar hoy usar el tutorial. La vista debe usar el controlador y DTO, sin modificar celdas o administrar Mementos. `Nivel` entrega copias independientes de su estado inicial, de modo que distintas partidas no comparten cajas ni posiciones. Las copias de Tablero son para aislar niveles; Undo y reinicio usan exclusivamente Memento.

## Reglas implementadas

Movimiento ortogonal, sin tirar, empujando una caja por vez. Paredes, límites, otra caja y caja contra pared bloquean la acción sin cambiar estado, contadores o historial. Un movimiento válido cuenta una vez y suma un empuje si corresponde. Victoria cuando todas las cajas están entregadas en destinos; no hay victoria vacía.

El controlador guarda el estado anterior solo tras confirmar un movimiento. Historial conserva hasta 15 Mementos. Cada Undo retira hasta 5 y restaura el más antiguo retirado; con menos de 5 deshace los disponibles. Máximo 3 usos seguidos; un movimiento válido reinicia la racha, uno bloqueado no. Historial vacío no consume uso. Movimientos y empujes no disminuyen al deshacer; los usos acumulados de Undo aumentan por uso efectivo.

Memento es inmutable y opaco al historial, sin getters públicos ni contadores. Solo Partida interpreta su contenido. Un token privado impide restaurar snapshots de otra partida. En este alcance todas las cajas son normales y la lista de agujeros tapados es vacía; resistencia y agujeros se incorporarán al implementar esas mecánicas.

Reiniciar restaura el Memento inicial y limpia historial. Se interpreta el reinicio como un intento nuevo: movimientos, empujes y ambos contadores de Undo vuelven a cero. Las decisiones no explicitan los contadores del reinicio; esta interpretación queda documentada aquí para revisión del equipo. Cambiar de nivel crea una Partida nueva y limpia el historial.

## Alcance restante

Pendientes: llaves/cerrojos/muros, frágiles, hielo, agujeros, cintas, portales, Observer, puntaje/Decorator, resumen/1UP, fábricas regionales, Facade, audio/Singleton y Swing. No se agregan clases vacías para simular esos patrones. No hay persistencia ni servidor en este entregable.

El [contrato TXT](FORMATO_NIVELES.md) es el documento para pasar al compañero de mapas. Se respetan sus archivos y los del responsable de textos, incluido README.

## Verificación

JUnit prueba la solución del tutorial, bloqueos sin efectos parciales, caminar/empujar, victoria, Undo parcial y de cinco, límite de quince, racha de tres usos, contadores, reinicio, cambio de nivel, aislamiento de partidas y snapshots, encabezados/capas inválidos, dimensiones máximas, BOM/CRLF y rechazo explícito de mecánicas pendientes. La consola permite comprobar el mismo recorrido mediante el JAR.

## Revisión de calidad antes de integrar

`clean verify` ejecuta las pruebas, verifica el formato con Spotless (Google Java Format, estilo AOSP) y analiza el código de producción con SpotBugs, esfuerzo máximo y umbral Low. Un fallo en cualquiera de los tres controles hace fallar el build. No se agregaron exclusiones de hallazgos de SpotBugs.

Para aplicar el formato después de editar Java:

```powershell
.\mvnw.cmd spotless:apply
.\mvnw.cmd clean verify
```

La revisión manual separó lectura de encabezados, validación de capas y construcción de objetos; centralizó captura/reconstrucción de cajas en `EstadoCaja`; reemplazó límites repetidos por constantes y consultas al modelo; restringió al paquete del núcleo el movimiento directo del jugador y el retiro de cajas. `Celda.setCaja` sigue público porque lo usa Factory Method, pero valida que no se sobrescriba una caja ni se coloque sobre terreno bloqueado. No se expone el tablero interno de una Partida a la vista.

SpotBugs detectó y se corrigieron el uso de codificación implícita al leer consola y un posible nombre de archivo nulo. La construcción de tablero ahora verifica estructura rectangular, celdas completas y posición inicial válida. Se agregaron regresiones para aislamiento de celdas/cajas, mutaciones inválidas, uso del Builder fuera de orden, marcadores duplicados y victoria con dos cajas.

Para continuar con otros tipos de caja, extender las ramas exhaustivas de `EstadoCaja.capturar` y `reconstruir`, además del creador y símbolo del tipo. Los terrenos actuales son inmutables y se comparten al copiar un nivel; al agregar muros o agujeros mutables, hay que copiar su estado para conservar el aislamiento. No se incorporan esas mecánicas en esta revisión.

Estos controles complementan la revisión manual; no prueban ausencia absoluta de bugs o problemas de diseño.

Resultado de esta revisión: **29 pruebas aprobadas**, Spotless aprobado y **0 hallazgos de SpotBugs** en el código de producción. También se verificó el JAR con el recorrido ganar → Undo → reiniciar.
