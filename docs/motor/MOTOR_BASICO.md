# Motor del juego

Empezó como el primer entregable del motor (movimiento, empuje, Undo y reinicio con cajas normales). Hoy tiene todas las mecánicas, los patrones del diagrama y está conectado con la vista Swing.

## Compilar y probar

Requiere **JDK 21** (`java` y `javac` disponibles; `JAVA_HOME` puede apuntar al JDK). Maven Wrapper fija Maven 3.9.9; no hace falta instalar Maven. La primera ejecución necesita Internet para descargar Maven, plugins y JUnit.

PowerShell, desde la raíz del repo:

```powershell
.\mvnw.cmd clean verify
java -jar target/temu-express-0.1.0-SNAPSHOT.jar
```

En Linux/macOS: `sh ./mvnw clean verify` y el mismo comando `java -jar`. El juego lee `assets/` y `niveles/` desde la raíz del repo.

La clase principal es `temuexpress.Aplicacion`:
- **Sin argumentos:** abre el juego completo en Swing, con los 6 niveles en orden.
- **Con un archivo de nivel:** abre una consola para probar el motor, por ejemplo `java -jar target/temu-express-0.1.0-SNAPSHOT.jar niveles/nivel_1-1.txt`. Se ingresa una acción por línea: A arriba, B abajo, I izquierda, D derecha, U Undo, R reiniciar, S salir. `src/test/resources/niveles/tutorial.txt` se resuelve con dos D.

## Arquitectura y contratos

Los nombres se tomaron de `docs/diagramas/TeMUExpress.mdj` y las reglas de `Decisiones_MVP_TeMU_ExpressFinal.txt`.

- `modelo.nucleo`: `Nivel`, `Tablero`, `Celda`, `Posicion`, `Direccion`, los terrenos (`Piso`, `Pared`, `Destino`, `Cerrojo`, `Muro`, `Agujero`, `Hielo`, `Cinta`, `Portal`), las cajas (`CajaNormal`, `CajaFragil`, `CajaLlave`), `Partida`, `MementoPartida`, `EstadoCaja` e `HistorialPartida`.
- `modelo.creacion`: `LectorNivel` dirige `ConstructorNivel` / `ConstructorNivelSokoban` (Builder). `CreadorCajaNormal`, `CreadorCajaFragil` y `CreadorCajaLlave` implementan Factory Method.
- `modelo.estrategias`: `EstrategiaDesplazamientoCaja` con `EstrategiaNormal`, `EstrategiaResbaladiza`, `EstrategiaCinta` y `EstrategiaPortal` (Strategy). El terreno de entrada elige la estrategia; esta calcula el destino sin modificar el tablero. `Encadenamiento` aplica el efecto de la celda siguiente cuando una caja sigue de largo.
- `modelo.observador`: `PublicadorPartida` y `ObservadorPartida` (Observer). `Partida` avisa cada acción con un `EventoPartidaDTO`.
- `modelo.puntaje`: `PuntajeBase` y sus decoradores `BonusSinUndo`, `BonusPorPar` y `RecargoUndo` (Decorator).
- `modelo.dto`: `TableroDTO` (ancho, alto y las dos capas como texto separado por `\n`), `EventoPartidaDTO` y `ResumenNivelDTO`, todos inmutables.
- `controlador`: `ControladorJuego` recibe lo que hace el jugador en la vista (implementa `OyenteVista`) y maneja el flujo de los niveles. `PreparadorNivelFacade` (Facade) lee el nivel, elige la fábrica regional, pone la música y le da al controlador los sprites, el fondo animado y la carpeta de voces de la región. `PresentadorPartida` y `ReaccionAudioPartida` son los observadores que actualizan la vista y el sonido.
- `recursos`: fábricas regionales (Abstract Factory), `GestorSonido` (Singleton) y el fondo animado.

La vista usa el controlador y los DTO, sin tocar celdas ni administrar Mementos. `Nivel` entrega copias independientes de su estado inicial, así distintas partidas no comparten cajas ni posiciones. Las copias de `Tablero` sirven para aislar niveles; Undo y reinicio usan solo Memento.

## Reglas implementadas

**Movimiento:** ortogonal, sin tirar, empujando una caja por vez. Paredes, límites, otra caja y caja contra pared bloquean la acción sin cambiar estado, contadores ni historial. Un movimiento válido cuenta una vez y suma un empuje si corresponde. El jugador no pisa agujeros abiertos, cintas, portales ni muros cerrados; sí camina sobre hielo.

**Victoria:** todas las cajas en su lugar. Las normales y las frágiles van a un destino; las llaves, a un cerrojo. No hay victoria vacía.

**Derrota:** se rompe una caja frágil o una caja llave cae en un agujero (el nivel queda imposible). Después de ganar o perder, el motor no acepta movimientos nuevos hasta usar Undo o reiniciar.

**Mecánicas:**
- **Caja llave, cerrojo y muros:** los muros se abren cuando todos los cerrojos tienen una caja llave encima, y se cierran si alguna sale. Para la caja que se está empujando, el estado se calcula sin contarla. Un muro con el jugador o una caja encima queda abierto hasta que se libera.
- **Caja frágil:** empieza con resistencia 4 y pierde 1 por empuje (un deslizamiento cuenta como un empuje). Aguanta 3 empujes; el cuarto la rompe.
- **Agujero:** una caja normal o frágil cae, desaparece y el agujero pasa a ser piso. Si cae una caja llave, es derrota.
- **Hielo:** la caja sigue en la dirección del empuje y aplica el efecto de la celda siguiente.
- **Cinta:** la caja sigue la dirección de la cinta; si está bloqueada, queda sobre la cinta.
- **Portal:** la caja sale por el portal de su par, en la misma dirección en que entró. Si la salida está bloqueada, el empuje se rechaza.

**Validación de niveles:** `LectorNivel` y `ConstructorNivelSokoban` rechazan, con un mensaje claro, archivos con encabezados o capas inválidos, mapas sin paredes alrededor, más de 13x7, sin un único jugador, con distinta cantidad de cajas llave que de cerrojos, muros sin cerrojo, portales sin pareja y cintas, hielo o portales que formen un ciclo.

**Undo (Memento):** el controlador guarda el estado anterior solo después de confirmar un movimiento. El historial conserva hasta 15 Mementos. Cada Undo retira hasta 5 y restaura el más antiguo retirado; con menos de 5, deshace los que haya. Máximo 3 usos seguidos; un movimiento válido reinicia la racha y uno bloqueado no. Con el historial vacío no se gasta un uso. Movimientos y empujes no bajan al deshacer; los usos de Undo suben por cada uso efectivo.

El Memento es inmutable y opaco para el historial: solo `Partida` interpreta su contenido. Guarda la posición del jugador, cada caja con su tipo y resistencia (`EstadoCaja`) y los agujeros tapados. Un token privado impide restaurar snapshots de otra partida.

**Reinicio:** restaura el Memento inicial, limpia el historial y pone en cero movimientos, empujes y ambos contadores de Undo (es un intento nuevo). Cambiar de nivel crea una `Partida` nueva y limpia el historial.

**Puntaje:** `PuntajeBase` = 1000 - 3 x movimientos - 5 x empujes - 100 x usos de Undo. `BonusSinUndo` suma 200 si no se usó Undo y `BonusPorPar` suma 150 si los movimientos no superan el PAR. En el 4-1, `RecargoUndo` descuenta 100 más por cada uso (el Undo cuesta el doble). El puntaje de un nivel nunca baja de 0.

**Eventos (Observer):** `MOVIMIENTO`, `EMPUJE`, `RESBALAR`, `CINTA`, `PORTAL`, `CAIDA`, `FRAGIL_DANO`, `ROTURA`, `META`, `MURO_ABRE`, `MURO_CIERRA`, `UNDO`, `REINICIO`, `VICTORIA` y `DERROTA`.

## Verificación

`clean verify` corre las pruebas, verifica el formato con Spotless (Google Java Format, estilo AOSP) y analiza el código con SpotBugs, esfuerzo máximo y umbral Low. Un fallo en cualquiera de los tres hace fallar el build. No hay exclusiones de hallazgos de SpotBugs.

Para aplicar el formato después de editar Java:

```powershell
.\mvnw.cmd spotless:apply
.\mvnw.cmd clean verify
```

Las **37 pruebas** (JUnit) cubren:
- `MotorBasicoTest`: tutorial, bloqueos sin efectos parciales, caminar y empujar, victoria, Undo parcial y de cinco, límite de quince, racha de tres usos, contadores, reinicio, cambio de nivel, aislamiento de partidas y snapshots, archivos inválidos y tamaño máximo.
- `MecanicasTest`: las soluciones de `niveles/SOLUCIONES.md` ganan cada uno de los 6 niveles; muros y caja llave; caja frágil (se rompe al cuarto empuje y Undo la recupera); caja llave en un agujero; portales; validación de llaves, cerrojos, portales sin pareja y ciclos de cintas.
- `IntegridadModeloTest`: reglas internas de `Celda`, `Tablero` y el Builder.
- `PatronesTest`: puntaje con decoradores y eventos del Observer.
- `FondoAnimadoTest`: el cielo de cada región se mueve y el tablero y el HUD no.

Estos controles complementan la revisión manual; no prueban ausencia absoluta de bugs ni de problemas de diseño.

El [contrato TXT](FORMATO_NIVELES.md) describe el formato de los niveles.
