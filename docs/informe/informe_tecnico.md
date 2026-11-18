# Informe técnico - TeMU Express (特穆快递)

**Materia:** Proceso de Desarrollo de Software (3.4.210) - UADE
**Docente:** Prof. Jonathan Leonel Pepe
**Equipo:** Los DesubiAmericanos
**Integrantes:**
- Ferreyra, Franco Elian - 1190726
- Obregon, Santiago - 1211437
- Diaz, Matias - 1209251
- Gonzalez Jimenez, Alejandra - 1198460
- Quijada, Jesús - 1195298
- Rodrigo, Juan Manuel - 1223897

**Repositorio:** https://github.com/Franco-Ferreyra/VideojuegoSokobanPDS-2026-MiTa

> Fuente principal: `Decisiones_MVP_TeMU_ExpressFinal.txt`, que prevalece sobre los demás documentos. Los nombres de clases y métodos coinciden con `docs/diagramas/TeMUExpress.mdj` y con el código.

---

## 1. Introducción

TeMU Express es una versión del Sokoban en Java 21 + Swing (2D). El jugador es un repartidor de una empresa de envíos, parodia de Temu, que lleva paquetes desde China hasta Sudamérica. En cada región el paquete sufre una traba real de la aduana: bloqueos, inspecciones, impuestos, papeleo, retenciones y correos que no funcionan. Cada traba se convierte en una mecánica del juego.

El objetivo del trabajo es aplicar los principios y patrones de la materia (SOLID, GRASP, MVC y GoF) en un juego completo y defendible.

## 2. Alcance del MVP

Seis niveles en formato región-subnivel:

| Nivel | Región | HUD | Mecánicas |
|---|---|---|---|
| 1-1 | China | CHINA | Cajas normales + llave, cerrojo y muros |
| 2-1 | Estados Unidos | EEUU | Caja frágil + llave, cerrojo y muros |
| 3-1 | Alemania | ALEMANIA | Agujeros + caja frágil |
| 4-1 | Argentina - Chile | ARG-CHILE | Resbaladizo (hielo) + cerrojo/muros + agujero + recargo de Undo |
| 4-2 | Ecuador - Colombia | ECU-COL | Cinta + caja frágil + cerrojo/muros |
| 4-3 | Perú - Venezuela | PERU-VEN | Portales + cinta + hielo + agujero + frágil + llave/cerrojo/muros (final) |

Cada nivel tiene una solución verificada y un PAR (la solución por 1,2, redondeado hacia arriba), documentados en `niveles/SOLUCIONES.md`. Una prueba automática reproduce cada solución y comprueba que gana el nivel.

**Incluye** todos los requisitos obligatorios de la consigna:
- Tablero con todos sus elementos y niveles en archivos .txt.
- Movimiento con flechas.
- Los tres tipos de caja.
- Undo y Reiniciar.
- Victoria, resumen con puntaje y HUD.
- Imágenes y sonidos.

**Funcionalidades adicionales:** tres, cada una con su patrón (sección 7), más mejoras de presentación.

**Fuera del MVP:**
- Editor de niveles, power-ups, clima y ranking.
- Logros, repetición y modo contrarreloj.
- Guardar y continuar la partida en archivo.

## 3. Reglas de juego e interpretaciones del equipo

La consigna deja algunos puntos abiertos. Estas son las decisiones del equipo (Decisiones, puntos 2 a 4).

### 3.1 Movimiento, cajas y terrenos

- **Movimiento:** con flechas. El jugador solo empuja, nunca tira. Empuja una caja por vez y no atraviesa cajas, paredes ni muros cerrados.
- **Victoria:** cada caja responde `estaEntregada()`.
  - Las cajas normales y frágiles se entregan sobre un destino; la caja llave, sobre un cerrojo.
  - Se gana cuando todas las cajas que quedan en el tablero cumplen.
- **Conteo de cajas:** lo valida el Builder.
  - Cajas normales + frágiles = destinos + agujeros.
  - Cajas llave = cerrojos.
  - Así, al ganar quedan ocupados todos los destinos y cerrojos.
- **Muros:** hay un solo grupo por nivel.
  - Se abren cuando todos los cerrojos tienen una caja llave encima.
  - Si se libera un cerrojo, los muros se vuelven a cerrar.
  - Un muro que en ese momento tenga encima al jugador o una caja queda abierto hasta que se libera.
- **Resbaladizo (hielo):** la caja sigue en la dirección del empuje y, al salir del hielo, aplica el efecto de la casilla siguiente (por ejemplo, caer en un agujero o seguir una cinta). El jugador camina sobre el hielo sin resbalar.
- **Caja frágil:**
  - Empieza con resistencia 4 y cada empuje le resta 1. Al llegar a 0 se rompe, así que aguanta 3 empujes y el cuarto la rompe.
  - Un deslizamiento cuenta como un solo empuje.
  - La vista muestra las grietas según la resistencia que le queda.
  - Si se rompe, el nivel se pierde y se ofrece Undo o Reiniciar.
- **Agujeros:**
  - La caja que cae desaparece y el agujero pasa a ser piso.
  - Hay una caja extra por cada agujero.
  - Si cae una caja llave, el nivel ya no se puede ganar y se ofrece Undo o Reiniciar.
- **Cinta:** tiene una dirección fija. La caja sigue la dirección de la cinta, no la del empuje. Si la salida está bloqueada, la caja queda sobre la cinta.
- **Portales:**
  - Van de a pares del mismo color, y cada par tiene un color distinto.
  - Solo pasan cajas. La caja sale por el portal de su par, en la misma dirección en que entró.
  - Si la salida está bloqueada, el empuje no se realiza.
- **Restricciones del jugador:** no puede pisar agujeros abiertos, cintas ni portales.
- **Tamaño del mapa:** máximo 13 columnas x 7 filas, paredes incluidas.

### 3.2 Undo y Reiniciar

La consigna dice: "deshacer los últimos 15 movimientos [...] 5 movimientos hacia atrás, hasta un máximo de tres usos consecutivos".

- Se guardan los últimos 15 movimientos confirmados. Cada uso retrocede 5, con un máximo de 3 usos seguidos.
- El contador de usos seguidos vuelve a 0 con cada movimiento nuevo.
- Con menos de 5 movimientos guardados, se deshacen los que haya y se consume un uso. Con el historial vacío no pasa nada.
- Los movimientos y empujes no bajan al deshacer. `usosUndo` suma 1 por cada uso efectivo.
- **Reiniciar** devuelve el nivel a su estado inicial y lo toma como un intento nuevo: vacía el historial y pone en 0 movimientos, empujes y usos de Undo.
- Cambiar de nivel también vacía el historial.

### 3.3 Puntaje (criterio del equipo)

- **Base:** `1000 - 3*movimientos - 5*empujes - 100*usosUndo`.
- **Bonus sin Undo:** +200 si no se usó Undo.
- **Bonus por par:** +150 si los movimientos no superan el par del nivel.
- **Recargo de Undo:** -100 adicional por cada uso, solo en 4-1 (el Undo cuesta el doble).
- Se aplica `max(0, resultado)` una sola vez, al final.

Ejemplo con 80 movimientos, 20 empujes, 1 Undo y par 90:
- Base: 560.
- Con el bonus por par queda en 710.
- En 4-1 queda en 610, por el recargo.

**Pantallas:**
- Al terminar cada nivel se muestra el resumen: movimientos, empujes, usos de Undo y puntaje final.
- El **1UP** suma los puntajes de los niveles terminados.
- Después del 4-3 hay un cierre en dos páginas: qué fue de cada repartidor y el puntaje total.

## 4. Arquitectura MVC (Clase 6)

Diagrama: `docs/diagramas/png/0 - Paquetes MVC.png`.

| Paquete | Responsabilidad | Contenido |
|---|---|---|
| `modelo` | Reglas del juego. No conoce Swing. | Subpaquetes `nucleo`, `creacion`, `estrategias`, `puntaje`, `observador` y `dto` |
| `vista` | Pantallas Swing. No contiene reglas. | `VentanaJuego`, tablero, HUD y pantallas de inicio, mensaje, juego, resumen, derrota y final |
| `controlador` | Recibe flechas y botones, llama al modelo y maneja el flujo de los niveles. | `ControladorJuego`, `PreparadorNivelFacade`, `PresentadorPartida`, `ReaccionAudioPartida` y `MensajesRegionales` |
| `recursos` | Imágenes, audio y fondo animado por región. | Abstract Factory regional, `GestorSonido` y `FondoAnimado` |

**Dependencias entre paquetes:**
- `controlador` depende de `vista`, `modelo` y `recursos`.
- `vista` depende de los DTO y de la interfaz `AnimacionFondo` de `recursos`. No conoce ninguna clase del modelo con reglas.
- `modelo` no depende de `vista`, `controlador` ni `recursos`, igual que el modelo del Casino de la Clase 6.

La vista nunca toca el modelo: recibe DTO inmutables. Por eso se puede cambiar la interfaz sin tocar las reglas, y probar las reglas sin abrir una ventana (las 37 pruebas corren sin Swing).

La comunicación vista → controlador va por la interfaz `OyenteVista` (`alPresionarFlecha`, `alPresionarUndo`, `alPresionarReiniciar`, `alContinuar`), y la de modelo → vista por la interfaz `VistaPartida`, que implementa `VentanaJuego`. Así ninguna capa depende de una clase concreta de la otra (inversión de dependencias).

**Flujo de un movimiento:**
1. El jugador presiona una flecha y la vista avisa a `OyenteVista.alPresionarFlecha`.
2. `ControladorJuego` crea un memento y llama a `Partida.mover(dir)`.
3. Si el movimiento es válido, guarda el memento en `HistorialPartida`.
4. `Partida` notifica a sus observadores con un `EventoPartidaDTO`.
5. `PresentadorPartida` actualiza la vista (`VistaPartida`) con el evento y el `TableroDTO`, y muestra la frase del repartidor cuando corresponde.
6. `ReaccionAudioPartida` pide el efecto o la voz a `GestorSonido`.
7. Si hubo victoria, el controlador calcula el puntaje con el Decorator y muestra el resumen.

## 5. Modelo del núcleo (diagrama 1)

Diagrama: `docs/diagramas/png/1 - Núcleo del modelo.png`.

- **Estructura:** `Partida` contiene un `Tablero`. El `Tablero` es una grilla de `Celda`, y cada celda tiene un `Terreno` y, a lo sumo, una `Caja`.
- **`Terreno`** es una interfaz implementada por `Piso`, `Pared`, `Destino`, `Cerrojo`, `Muro`, `Agujero`, `Hielo`, `Cinta` y `Portal`. Cada terreno responde por sí mismo:
  - `esTransitablePorJugador()`
  - `esTransitablePorCaja()`
  - `esDestino()`
  - `esCerrojo()`
  - `getEstrategia()`
  - `getSimbolo()`, para armar el `TableroDTO`.
  - `copiar()`: los terrenos sin estado se comparten y los que tienen estado (`Muro`, `Agujero`) se copian, para que dos partidas no se afecten.
- **`Caja`** es una interfaz implementada por `CajaNormal`, `CajaFragil` y `CajaLlave`, con los métodos `estaEntregada()`, `alSerEmpujada()`, `estaRota()` y `getTipo()`.
- **Tipos de apoyo:** `Posicion` es un valor inmutable con `siguiente(dir)`, `Direccion` es un enum de las 4 flechas y `TipoCaja` es otro enum. `Portal` guarda la `Posicion` de su pareja.

Gracias al polimorfismo, el recorrido de la caja (la estrategia de cada terreno), quién puede pasar por cada casilla (`esTransitablePorJugador()`, `esTransitablePorCaja()`) y la entrega (`estaEntregada()` de cada caja) se resuelven sin condicionales en `Partida`. `Partida` solo consulta el tipo concreto en estados propios de algunos terrenos y cajas: tapar agujeros, abrir y cerrar muros, la resistencia de la caja frágil para el `TableroDTO` y el tipo de empuje que se informa al sonido.

## 6. Patrones de diseño

Cada patrón tiene su diagrama en `docs/diagramas/png/` (11 diagramas, del 0 al 10). Strategy, Memento y Singleton son obligatorios por pedido del profesor. Prototype no se usa, por recomendación del profesor, y Command tampoco: Undo y Reiniciar se resuelven solo con Memento.

La justificación de cada patrón sigue a Shvets, *Dive Into Design Patterns*: para cada uno se indica el problema, la solución y el principio que lo respalda.

### 6.1 Factory Method - creación de cajas (diagrama 2)

- **Problema:** el lector de niveles tiene que crear cajas normales, frágiles y llave sin quedar acoplado a cada constructor.
- **Solución:** `CreadorCaja` declara `crearCaja()`, y cada creador concreto decide qué caja instanciar. Por ejemplo, `CreadorCajaFragil` devuelve una `CajaFragil` con resistencia 4.
- **Clases:**
  - Creator: `CreadorCaja`.
  - Creadores concretos: `CreadorCajaNormal`, `CreadorCajaFragil` y `CreadorCajaLlave`.
  - Producto: `Caja`.
  - Cliente: `LectorNivel`, que elige el creador según el símbolo del TXT (`$`, `F`, `K`).
- **Justificación:** sumar un tipo de caja nuevo implica agregar su producto y su creador, sin modificar el lector (OCP).

### 6.2 Builder - construcción del nivel (diagrama 3)

- **Problema:** un nivel reúne dimensiones, terrenos, cajas, jugador y vínculos (cerrojos con muros, portales con su par). Un constructor enorme sería ilegible y permitiría dejar partes sin armar.
- **Solución:** `LectorNivel` interpreta el .txt y dirige a un `ConstructorNivel` paso a paso:
  1. `reiniciar()`
  2. `configurarNivel()`
  3. `construirTablero()`
  4. `agregarTerreno()`
  5. `agregarCaja()`
  6. `colocarJugador()`
  7. `vincularElementos()`
  8. `obtenerResultado()`
- **Clases:**
  - Director: `LectorNivel`.
  - Builder: `ConstructorNivel`.
  - Builder concreto: `ConstructorNivelSokoban`.
  - Producto: `Nivel`, con número, región, variante, par y tablero.
- **Validaciones del Builder** (cada una con un mensaje de error claro):
  - Tamaño máximo 13x7, mapa rodeado de paredes y un único jugador.
  - Conteo de cajas: llaves = cerrojos, y muros solo si hay cerrojos.
  - Portales emparejados.
  - Cintas, hielo y portales que no formen un ciclo.
- **Formato TXT** (`docs/motor/FORMATO_NIVELES.md`):
  - Encabezado con `REGION`, `VARIANTE` y `PAR`.
  - Capa `[TERRENO]`: `#` pared, espacio piso, `.` destino, `L` cerrojo, `W` muro, `O` agujero, `~` hielo, `> < ^ v` cinta y `1-9` portales.
  - Capa `[OBJETOS]`: `@` jugador, `$` caja normal, `F` caja frágil y `K` caja llave.

### 6.3 Abstract Factory - regiones temáticas (diagrama 4)

- **Problema:** cada región tiene su propio repartidor, escenario (fondo, pared, piso, destino, muros y animación del cielo) y sonido (música y voces). Si se crean por separado, se pueden mezclar elementos de distintas regiones.
- **Solución:** `FabricaNivel` crea la familia completa: `crearRepartidor()`, `crearEscenario()` y `crearSonido()`.
- **Clases:**
  - Fábricas concretas: `FabricaChina`, `FabricaEEUU`, `FabricaAlemania` y `FabricaSudamerica`. Esta última recibe la variante: ARG-CHILE, ECU-COL o PERU-VEN.
  - Productos abstractos: `AspectoRepartidor`, `Escenario` y `SonidoRegional`.
  - Clases base abstractas: `AspectoRepartidorColor`, `EscenarioCarpeta` y `SonidoArchivos`. Leen las imágenes y los audios de la carpeta de la región, así cada producto concreto solo indica su carpeta o su color, sin repetir código.
  - Productos concretos: uno de cada producto por región, por ejemplo `EscenarioChina`. Cada escenario arma además su `AnimacionFondo` (sección 7.1).
- **Justificación:** agregar una región es agregar una fábrica y sus productos, sin tocar el motor del juego. Las cajas no son regionales: el paquete TeMU es común a todas.

### 6.4 Singleton y Facade (diagrama 5)

**Singleton - `GestorSonido`**
- **Problema:** si cada pantalla tuviera su propio gestor, sonarían varias músicas a la vez y las voces se pisarían.
- **Solución:** `GestorSonido` es un `enum` con una sola constante, `INSTANCIA`, y `getInstancia()`. Es la forma recomendada en Java: la JVM garantiza una única instancia, incluso con varios hilos y con serialización.
- **Responsabilidades:** música de la región, volumen, efectos y voces. Reproduce el audio en un hilo propio para no trabar la pantalla, carga los sonidos una sola vez y maneja prioridades: una sola voz y un solo efecto a la vez, y la música baja de volumen mientras habla un personaje.
- **Límite:** el Singleton se usa solo para el audio.

**Facade - `PreparadorNivelFacade`**
- **Problema:** iniciar un nivel requiere coordinar varios subsistemas. Si el controlador los conociera a todos, quedaría acoplado a cada uno.
- **Solución:** `prepararNivel(numero)` coordina:
  - `LectorNivel`, que usa el Builder.
  - `FabricaNivel`, la Abstract Factory, elegida según la región del nivel.
  - `GestorSonido`, que pone la música de la región.

  Con eso devuelve el `Nivel` listo. Después le da al controlador lo que la vista necesita de esa región: `armarSprites()`, `getAnimacionFondo()` y `getCarpetaVoces()`. La fábrica nunca sale de la fachada, así que el controlador solo habla con ella (Shvets: el cliente usa la fachada en lugar de llamar a los subsistemas; Clase 11, `FachadaMercader`).
- **Ubicación:** está en el paquete `controlador`, porque coordina recursos (fábrica y audio) y arma las imágenes de la vista. Así el modelo no depende de `recursos`.

### 6.5 Strategy - desplazamiento de cajas (diagrama 6)

- **Problema:** el recorrido de una caja depende del terreno: puede avanzar una casilla, deslizarse, seguir una cinta o atravesar un portal. Concentrarlo todo en `Partida` llenaría el código de condicionales.
- **Solución:** `EstrategiaDesplazamientoCaja.calcularDestino(tablero, origen, dir)` tiene cuatro implementaciones: `EstrategiaNormal`, `EstrategiaResbaladiza`, `EstrategiaCinta` y `EstrategiaPortal`.
  - Cada `Terreno` devuelve su estrategia con `getEstrategia()`.
  - El contexto es `Partida`, que al empujar usa la estrategia del terreno al que entra la caja.
  - Los efectos se encadenan con la clase de apoyo `Encadenamiento`: cuando una caja sigue de largo (hielo, cinta o portal), le pide a la casilla siguiente su propia estrategia. Los ciclos se descartan al cargar el nivel.
- **Justificación:** agregar un terreno con otro recorrido es agregar su clase y su estrategia, sin tocar el cálculo del recorrido en `Partida`. La estrategia se elige por el terreno, no por el país.

### 6.6 Memento - Undo y Reiniciar (diagrama 7)

- **Problema:** Undo tiene que devolver el nivel a un estado anterior aunque se haya roto una caja, tapado un agujero o abierto un muro, sin exponer los atributos privados de `Partida`.
- **Solución y roles:**
  - **Originator:** `Partida`, con `crearMemento()`, `restaurar(m)`, `registrarUsoUndo()` y `reiniciar()`, que restaura el memento inicial.
  - **Memento:** `MementoPartida`, inmutable. Guarda la posición del jugador, cada caja como un `EstadoCaja` (un `record` con tipo, posición y resistencia) y los agujeros tapados.
    - Los muros no se guardan porque se recalculan a partir de los cerrojos.
    - Sus getters tienen visibilidad de paquete, y un dato privado (`getPropietario()`) impide restaurar un memento en una partida que no lo creó.
  - **Caretaker:** `HistorialPartida`. Guarda hasta 15 mementos, nunca lee su contenido y no conoce a `Partida`.
- **Flujo:**
  - **Mover:** se crea un memento antes de mover y se guarda solo si el movimiento fue válido.
  - **Undo:** `historial.deshacer()` saca hasta 5 mementos y devuelve el más antiguo. Después se llama a `partida.restaurar(m)`.
  - **Reiniciar:** `partida.reiniciar()` e `historial.limpiar()`.

### 6.7 Observer - notificaciones de la partida (diagrama 8)

- **Problema:** después de cada cambio hay que actualizar el tablero y el HUD, mostrar la frase del repartidor y reproducir un sonido. Si `Partida` llamara directamente a la vista y al audio, el modelo dependería de Swing.
- **Solución:** `Partida` implementa `PublicadorPartida` (`suscribir`, `desuscribir` y `notificar`). Los observadores implementan `ObservadorPartida.actualizar(EventoPartidaDTO)`.
  - `PresentadorPartida` actualiza la vista a través de la interfaz `VistaPartida` y toma las frases de `MensajesRegionales`.
  - `ReaccionAudioPartida` elige el efecto y la voz de cada evento según su prioridad.
  - El evento distingue: `MOVIMIENTO`, `EMPUJE`, `RESBALAR`, `CINTA`, `PORTAL`, `CAIDA`, `FRAGIL_DANO`, `ROTURA`, `META`, `MURO_ABRE`, `MURO_CIERRA`, `UNDO`, `REINICIO`, `VICTORIA` y `DERROTA`.
- **Justificación:** se puede sumar una reacción nueva sin tocar las reglas, por ejemplo un sistema de logros en el futuro.

### 6.8 Decorator - bonificaciones de puntaje (diagrama 9)

- **Problema:** el puntaje combina una base con bonus y recargos según el nivel. Tener una clase por cada combinación multiplicaría las variantes.
- **Solución:**
  - Componente: la interfaz `CalculadorPuntaje.calcular(resumen)`.
  - Componente concreto: `PuntajeBase`.
  - Decorador base: `DecoradorPuntaje`, que envuelve a otro calculador.
  - Decoradores concretos: `BonusSinUndo`, `BonusPorPar` y `RecargoUndo`.
- **Ejemplo en 4-1:** `RecargoUndo → BonusPorPar → BonusSinUndo → PuntajeBase`. En los demás niveles se omite `RecargoUndo`.

### 6.9 Bridge - solo documentado

Hay una sola vista de juego con el mismo layout en todas las regiones. Sin un editor no existen dos dimensiones de variación, así que se documenta y se incorporaría solo si se agrega un editor (vista de juego vs. vista de edición, por cada región).

## 7. Funcionalidades adicionales

| Funcionalidad | Patrón | Qué agrega |
|---|---|---|
| Regiones temáticas | Abstract Factory | Cada región cambia repartidor, escenario (fondo, pared, piso, destino, muros y cielo animado), música y voces. Además tiene sus mensajes propios. |
| Terrenos especiales | Strategy | Agujeros, cinta y portales, además del hielo obligatorio |
| Sistema de bonificaciones TeMU | Decorator | Bonus sin Undo, bonus por par y recargo de Undo en 4-1, combinables |

### 7.1 Mejoras de presentación

- **Fondo animado por región (diagrama 10):** el cielo de cada región se mueve: nubes, farolitos en China, reflectores y luces de Las Vegas en EEUU, un zepelín al atardecer en Alemania, nieve en Argentina - Chile, loros en Ecuador - Colombia, y la cascada del Salto Ángel con helicópteros y un cóndor en Perú - Venezuela.
  - `FondoAnimado` implementa la interfaz `AnimacionFondo` y agrega una lista de `Capa`. Cada capa (`CapaNubes`, `CapaNieve`, `CapaCascada`, etc.) pinta un efecto sobre un `Cuadro`.
  - Agregar un efecto nuevo es agregar una clase que implemente `Capa`, sin tocar las demás (OCP). La vista solo conoce la interfaz `AnimacionFondo` (programar contra una interfaz), y el fondo se arma por composición de capas en lugar de herencia.
  - El tablero y el HUD no se animan: una prueba lo verifica.
- **Portada animada:** el logo flota con un brillo que lo cruza y "ENTER para empezar" titila.
- **Historia de los repartidores:** cada nivel abre con la historia del repartidor de la región, y el repartidor comenta las entregas, las caídas y las derrotas con frases propias (`docs/textos/mensajes_regionales.md`).
- **Audio con prioridades:** las voces y los efectos no se pisan (sección 6.4).

## 8. Principios SOLID

| Principio | Ejemplo concreto en el diseño |
|---|---|
| **S** - Responsabilidad única | `HistorialPartida` solo guarda mementos; `LectorNivel` solo interpreta el TXT; `GestorSonido` solo reproduce audio; `MensajesRegionales` solo tiene los textos; la vista solo dibuja DTO. |
| **O** - Abierto/cerrado | Un terreno con otro recorrido se agrega con su clase `Terreno` y su estrategia, sin tocar el cálculo del recorrido en `Partida`. Un bonus nuevo es otro decorador. Una región nueva es otra fábrica. Un efecto del fondo es otra `Capa`. |
| **L** - Sustitución de Liskov | `Partida` trata cualquier `Caja` o `Terreno` por su interfaz: una `CajaFragil` se empuja igual que una `CajaNormal`. |
| **I** - Segregación de interfaces | Los contratos son chicos y específicos: `ObservadorPartida` tiene un solo método, separado de `PublicadorPartida`; `CalculadorPuntaje` y `Capa` también tienen uno solo; `OyenteVista` y `VistaPartida` separan lo que la vista avisa de lo que muestra. |
| **D** - Inversión de dependencias | `Partida` depende de `ObservadorPartida` y de `EstrategiaDesplazamientoCaja`, no de las clases concretas. `LectorNivel` depende de `ConstructorNivel` y `CreadorCaja`. El controlador y la vista se hablan por `OyenteVista` y `VistaPartida`. |

Además se aplicaron los principios generales de Shvets: **encapsular lo que varía** (el recorrido de la caja en Strategy, el puntaje en Decorator, la región en Abstract Factory), **programar contra interfaces** y **composición sobre herencia** (decoradores, capas del fondo y estrategias se combinan en lugar de heredar).

## 9. Patrones GRASP

| Patrón GRASP | Ejemplo concreto en el diseño |
|---|---|
| **Experto en información** | Cada caja sabe si está entregada (`estaEntregada()`). `CajaFragil` maneja su propia resistencia. `Tablero` conoce las posiciones. |
| **Creador** | `ConstructorNivelSokoban` crea el `Tablero` y sus celdas porque tiene los datos para armarlos. `Partida` crea sus mementos. |
| **Controlador** | `ControladorJuego` recibe los eventos del sistema (flechas, UNDO, REINICIAR y ENTER) y los delega al modelo. |
| **Bajo acoplamiento** | La vista solo conoce DTO e interfaces. Observer evita que el modelo conozca Swing. La fachada oculta los subsistemas de carga. |
| **Alta cohesión** | Cada paquete del modelo agrupa una sola cosa: núcleo, creación, estrategias, puntaje u observador. |
| **Polimorfismo** | Cada terreno resuelve su recorrido (estrategia) y si se puede pasar; cada caja, si está entregada y si está rota. |
| **Fabricación pura** | `HistorialPartida`, `LectorNivel`, `GestorSonido` y `Encadenamiento` no representan algo del dominio; existen para mantener la cohesión. |
| **Indirección** | `PreparadorNivelFacade` media entre el controlador y los subsistemas. `PresentadorPartida` media entre el modelo y la vista. |
| **Variaciones protegidas** | Las interfaces `Terreno`, `Caja`, `EstrategiaDesplazamientoCaja`, `FabricaNivel` y `AnimacionFondo` protegen al resto del diseño de los cambios en sus implementaciones. |

## 10. Calidad y herramientas

- **Lenguaje y build:** Java 21, Swing y Maven (con Maven Wrapper, no hace falta instalarlo). Cómo ejecutar: `README.md`.
- **Pruebas:** 37 pruebas con JUnit 5. Cubren el motor (movimiento, bloqueos, Undo, reinicio, aislamiento de partidas), las mecánicas (cada solución de `SOLUCIONES.md` gana su nivel, caja frágil, caja llave en agujero, portales, validaciones), los patrones (puntaje y eventos) y el fondo animado.
- **Formato:** Spotless con Google Java Format (estilo AOSP).
- **Análisis estático:** SpotBugs con esfuerzo máximo, umbral bajo y sin exclusiones.
- `mvnw clean verify` corre los tres controles; si alguno falla, el build falla.
- **Diseño:** StarUML (`docs/diagramas/TeMUExpress.mdj`, 11 diagramas con sus PNG).
- **Control de versiones:** Git y GitHub.
- **Assets:** Python con Pillow para generar los sprites, y VoiceStudio para el audio.

## 11. Assets

Todos los assets son del equipo y se liberan como dominio público (CC0). El detalle de cada archivo está en `ASSETS.md`.

- **Imágenes:** pixel art propio de 16x16 en `assets/img/`.
  - Los tiles y fondos de las 6 regiones están basados en los bocetos del grupo.
  - El repartidor viene del diseño de un integrante, con un color por familia y su esqueleto para la derrota.
  - Los sprites del fondo animado se generaron con scripts de Python del equipo.
- **Audio:** música, efectos y voces en `assets/audio/`, producidos por un integrante con la herramienta de código abierto VoiceStudio, usando su propia voz como única fuente. No se usaron grabaciones ni samples de terceros.
- No se usa ningún logo, imagen ni sonido de la marca Temu: "TeMU Express" es una parodia hecha por el equipo.

## 12. Declaración del uso de IA

La consigna permite usar IA como apoyo, siempre que se declare. El equipo usó:

- **Claude (Anthropic), mediante Claude Code:**
  - Implementación asistida del código: mecánicas, patrones, vista Swing, fondo animado, audio y pruebas.
  - Revisión y alineación de los diagramas de StarUML con el código y la teoría.
  - Redacción y corrección de los documentos de diseño, los textos del juego y este informe.
  - Generación de los sprites en pixel art a partir de los bocetos del grupo, y conversión del personaje diseñado por el equipo.
  - Verificación de los niveles con un solucionador, para calcular la solución y el PAR.
- **ChatGPT (OpenAI):** el mismo tipo de apoyo que Claude: implementación asistida del código, revisión de diagramas y redacción de documentos.
- **Gemini (Google):** creación de bocetos y apoyo para explicar y desarrollar las ideas de cada temática del juego (regiones, personajes y mecánicas).
- **VoiceStudio:** procesamiento de la voz de un integrante para generar las voces, los efectos y la música.

El equipo revisó, adaptó y validó todo lo generado. Las decisiones de diseño son del equipo, y cada integrante puede explicarlas y defenderlas.

## 13. Conclusiones

**Aprendizajes**
- La aplicación de los patrones de diseño resultó más clara al partir de problemas concretos del dominio que de su definición teórica. Strategy surgió de la necesidad de que cada terreno determinara el recorrido de la caja sin concentrar esa lógica en `Partida`, y Memento, de la necesidad de revertir estados complejos como roturas de cajas, agujeros tapados y muros abiertos.
- La arquitectura MVC, junto con el uso de DTO inmutables entre capas, permitió desarrollar la vista y el modelo en paralelo. La interfaz gráfica se construyó y validó con datos de prueba antes de que el motor contara con todas las mecánicas, y luego se integró sin modificar sus reglas.
- Las pruebas automatizadas fueron una red de seguridad para evolucionar el código. Cada mecánica nueva se validó reproduciendo la solución completa de los seis niveles, lo que permitió detectar regresiones de inmediato.

**Principales desafíos**
- **Acotar el alcance.** La propuesta original contemplaba doce niveles, tres por región, para representar con mayor detalle las problemáticas aduaneras reales de cada país. Para ajustarse a los tiempos del trabajo práctico, el alcance se redujo a seis niveles: uno por región en China, Estados Unidos y Alemania, y tres en Sudamérica, donde cada nivel agrupa dos países con problemáticas afines. Este recorte priorizó la calidad de las mecánicas, del diseño y de la documentación por sobre la cantidad de contenido, sin perder la identidad temática del juego.
- **El encadenamiento de mecánicas.** Resolver correctamente las combinaciones de terrenos (por ejemplo, una caja que se desliza por el hielo, ingresa a una cinta y termina en un portal) y garantizar que Undo restaurara con exactitud todo ese estado.
- **El diseño de niveles.** Lograr mapas resolubles y desafiantes dentro del límite de 13x7 casillas requirió desarrollar un solucionador automático para verificar cada nivel y calcular su PAR.
- **La sincronización del audio.** Coordinar voces, efectos y música para evitar superposiciones y demoras implicó centralizar la reproducción en un hilo dedicado con un esquema de prioridades.

**Qué se haría distinto**
- Definir desde el inicio el contrato entre el front y el back (formato de los DTO y tipos de evento), para reducir los ajustes durante la integración.
- Escribir las pruebas de cada mecánica antes de implementarla, siguiendo un enfoque guiado por pruebas.

**Líneas de trabajo futuras**
- Recuperar el alcance original de doce niveles, con más mecánicas basadas en casos reales de cada país.
- Un editor de niveles, incorporando Bridge, que ya está documentado.
- Un sistema de logros, implementado como un nuevo observador de la partida.
- Guardado de partida y ranking de puntajes.
