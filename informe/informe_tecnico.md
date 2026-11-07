# Informe técnico - TeMU Express (特穆快递)

**Materia:** Proceso de Desarrollo de Software (3.4.210) - UADE
**Docente:** Prof. Jonathan Leonel Pepe
**Equipo:** Los DesubiAmericanos
**Integrantes:** [Apellido, Nombre] · [Apellido, Nombre] · [Apellido, Nombre] · [Apellido, Nombre] · [Apellido, Nombre]
**Repositorio:** https://github.com/Franco-Ferreyra/VideojuegoSokobanPDS-2026-MiTa

> Esqueleto con contenido. Las secciones marcadas con **[PENDIENTE]** se completan cuando el código esté terminado. Fuente principal: `Decisiones_MVP_TeMU_ExpressFinal.txt`, que prevalece sobre los demás documentos.

---

## 1. Introducción

TeMU Express es una versión del Sokoban en Java + Swing (2D). El jugador es un repartidor de una empresa de envíos, parodia de Temu, que lleva paquetes desde China hasta Sudamérica. En cada región el paquete sufre una traba real de la aduana: bloqueos, inspecciones, impuestos, papeleo, retenciones y correos que no funcionan. Cada traba se convierte en una mecánica del juego.

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

**Incluye** todos los requisitos obligatorios de la consigna:
- Tablero con todos sus elementos y niveles en archivos .txt.
- Movimiento con flechas.
- Los tres tipos de caja.
- Undo y Reiniciar.
- Victoria, resumen con puntaje y HUD.
- Imágenes y sonidos.

**Funcionalidades adicionales:** se agregan tres, cada una con su patrón (sección 7).

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
- **Resbaladizo (hielo):** la caja sigue en la dirección del empuje hasta chocar o llegar a una casilla que no es hielo. El jugador no resbala.
- **Caja frágil:**
  - Empieza con resistencia 4 y cada empuje le resta 1. Al llegar a 0 se rompe, así que aguanta 3 empujes y el cuarto la rompe.
  - Un deslizamiento largo cuenta como un solo empuje.
  - Si se rompe, el nivel se pierde y se ofrece Undo o Reiniciar.
- **Agujeros:**
  - La caja que cae desaparece y el agujero pasa a ser piso.
  - Hay una caja extra por cada agujero.
  - Si cae una caja llave, el nivel ya no se puede ganar y se ofrece Undo o Reiniciar.
- **Cinta:** tiene una dirección fija. La caja sigue la dirección de la cinta, no la del empuje.
- **Portales:**
  - Van de a pares del mismo color, y cada par tiene un color distinto.
  - Solo pasan cajas. La caja sale en la dirección del empuje.
  - Si la salida está bloqueada, el empuje no se realiza.
- **Restricciones del jugador:** no puede pisar agujeros abiertos, cintas ni portales.
- **Tamaño del mapa:** máximo 13 columnas x 7 filas, paredes incluidas.

### 3.2 Undo y Reiniciar

La consigna dice: "deshacer los últimos 15 movimientos [...] 5 movimientos hacia atrás, hasta un máximo de tres usos consecutivos".

- Se guardan los últimos 15 movimientos confirmados. Cada uso retrocede 5, con un máximo de 3 usos seguidos.
- El contador de usos seguidos vuelve a 0 con cada movimiento nuevo.
- Con menos de 5 movimientos guardados, se deshacen los que haya y se consume un uso. Con el historial vacío no pasa nada.
- Los movimientos y empujes no bajan al deshacer. `usosUndo` suma 1 por cada uso efectivo.
- Reiniciar o cambiar de nivel vacía el historial.

### 3.3 Puntaje (criterio del equipo)

- **Base:** `1000 - 3*movimientos - 5*empujes - 100*usosUndo`.
- **Bonus sin Undo:** +200 si no se usó Undo.
- **Bonus por par:** +150 si los movimientos no superan el par del nivel.
- **Recargo de Undo:** -100 adicional por cada uso, solo en 4-1.
- Se aplica `max(0, resultado)` una sola vez, al final.

Ejemplo con 80 movimientos, 20 empujes, 1 Undo y par 90:
- Base: 560.
- Con el bonus por par queda en 710.
- En 4-1 queda en 610, por el recargo.

**Pantallas:**
- Al terminar cada nivel se muestra el resumen: movimientos, empujes, usos de Undo y puntaje final.
- El **1UP** suma los puntajes de los niveles terminados.

## 4. Arquitectura MVC (Clase 6)

Diagrama: `diagramas/png/0 - Paquetes MVC.png`.

| Paquete | Responsabilidad | Contenido |
|---|---|---|
| `modelo` | Reglas del juego. No conoce Swing. | Subpaquetes `nucleo`, `creacion`, `estrategias`, `puntaje` y `observador` |
| `vista` | Pantallas Swing. No contiene reglas. | Tablero, HUD y pantallas de resumen, derrota y final |
| `controlador` | Recibe flechas y botones, llama al modelo y actualiza la vista. | `ControladorJuego`, `PresentadorPartida` y `ReaccionAudioPartida` |
| `dto` | Datos sin comportamiento entre capas. | `EventoPartidaDTO`, `TableroDTO` y `ResumenNivelDTO` |
| `recursos` | Imágenes y audio por región. | Abstract Factory regional y `GestorSonido` |

**Dependencias entre paquetes:**
- `controlador` depende de `vista`, `modelo`, `dto` y `recursos`.
- `vista` depende solo de `dto`.
- `modelo` depende de `dto` y `recursos`.

La vista nunca toca el modelo: recibe DTO inmutables. Por eso se puede cambiar la interfaz sin tocar las reglas, y probar las reglas sin abrir una ventana.

**Flujo de un movimiento:**
1. El jugador presiona una flecha.
2. `ControladorJuego` crea un memento y llama a `Partida.mover(dir)`.
3. Si el movimiento es válido, guarda el memento en `HistorialPartida`.
4. `Partida` notifica a sus observadores.
5. `PresentadorPartida` actualiza la vista con `EventoPartidaDTO` y `TableroDTO`.
6. `ReaccionAudioPartida` pide el sonido a `GestorSonido`.

## 5. Modelo del núcleo (diagrama 1)

Diagrama: `diagramas/png/1 - Núcleo del modelo.png`.

- **Estructura:** `Partida` contiene un `Tablero`. El `Tablero` es una grilla de `Celda`, y cada celda tiene un `Terreno` y, a lo sumo, una `Caja`.
- **`Terreno`** es una interfaz implementada por `Piso`, `Pared`, `Destino`, `Cerrojo`, `Muro`, `Agujero`, `Hielo`, `Cinta` y `Portal`. Cada terreno responde por sí mismo:
  - `esTransitablePorJugador()`
  - `esTransitablePorCaja()`
  - `esDestino()`
  - `esCerrojo()`
  - `getEstrategia()`
- **`Caja`** es una interfaz implementada por `CajaNormal`, `CajaFragil` y `CajaLlave`, con los métodos `estaEntregada()`, `alSerEmpujada()`, `estaRota()` y `getTipo()`.
- **Tipos de apoyo:** `Posicion` es un valor inmutable con `siguiente(dir)`, `Direccion` es un enum de las 4 flechas y `TipoCaja` es otro enum.

Gracias al polimorfismo, `Partida` no tiene un `switch` por tipo de terreno ni de caja.

## 6. Patrones de diseño

Cada patrón tiene su diagrama en `diagramas/png/`. Strategy, Memento y Singleton son obligatorios por pedido del profesor. Prototype no se usa, por recomendación del profesor, y Command tampoco: Undo y Reiniciar se resuelven solo con Memento.

### 6.1 Factory Method - creación de cajas (diagrama 2)

- **Problema:** el lector de niveles tiene que crear cajas normales, frágiles y llave sin quedar acoplado a cada constructor.
- **Solución:** `CreadorCaja` declara `crearCaja()`, y cada creador concreto decide qué caja instanciar. Por ejemplo, `CreadorCajaFragil` devuelve una `CajaFragil` con resistencia 4.
- **Clases:**
  - Creator: `CreadorCaja`.
  - Creadores concretos: `CreadorCajaNormal`, `CreadorCajaFragil` y `CreadorCajaLlave`.
  - Producto: `Caja`.
  - Cliente: `LectorNivel`.
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
- **Validaciones del Builder:**
  - Tamaño máximo 13x7 y un único jugador.
  - Conteo de cajas.
  - Portales emparejados y destinos que no estén sobre terrenos especiales.
- **Formato TXT:**
  - Encabezado con `REGION`, `VARIANTE` y `PAR`.
  - Capa `[TERRENO]`: `#` pared, espacio piso, `.` destino, `L` cerrojo, `W` muro, `O` agujero, `~` hielo, `> < ^ v` cinta y `1-9` portales.
  - Capa `[OBJETOS]`: `@` jugador, `$` caja normal, `F` caja frágil y `K` caja llave.

### 6.3 Abstract Factory - regiones temáticas (diagrama 4)

- **Problema:** cada región tiene su propio repartidor, escenario (fondo, pared, piso, destino y muros) y sonido. Si se crean por separado, se pueden mezclar elementos de distintas regiones.
- **Solución:** `FabricaNivel` crea la familia completa: `crearRepartidor()`, `crearEscenario()` y `crearSonido()`.
- **Clases:**
  - Fábricas concretas: `FabricaChina`, `FabricaEEUU`, `FabricaAlemania` y `FabricaSudamerica`. Esta última recibe la variante: ARG-CHILE, ECU-COL o PERU-VEN.
  - Productos abstractos: `AspectoRepartidor`, `Escenario` y `SonidoRegional`.
  - Productos concretos: uno de cada producto por región, por ejemplo `EscenarioChina`.
- **Justificación:** agregar una región es agregar una fábrica y sus productos, sin tocar el motor del juego. Las cajas no son regionales: el paquete TeMU es común a todas.

### 6.4 Singleton y Facade (diagrama 5)

**Singleton - `GestorSonido`**
- **Problema:** si cada pantalla tuviera su propio gestor, sonarían varias músicas a la vez.
- **Solución:** un constructor privado y `getInstancia()`. El gestor centraliza la música actual, el volumen, los efectos y las voces.
- **Límite:** el Singleton se usa solo para el audio.

**Facade - `PreparadorNivelFacade`**
- **Problema:** iniciar un nivel requiere coordinar varios subsistemas. Si el controlador los conociera a todos, quedaría acoplado a cada uno.
- **Solución:** `prepararNivel(numero)` coordina:
  - `LectorNivel`, que usa el Builder.
  - `FabricaNivel`, la Abstract Factory.
  - `GestorSonido`, que pone la música de la región.

  Con eso devuelve el `Nivel` listo, y el controlador solo llama a la fachada.

### 6.5 Strategy - desplazamiento de cajas (diagrama 6)

- **Problema:** el recorrido de una caja depende del terreno: puede avanzar una casilla, deslizarse, seguir una cinta o atravesar un portal. Concentrarlo todo en `Partida` llenaría el código de condicionales.
- **Solución:** `EstrategiaDesplazamientoCaja.calcularDestino(tablero, origen, dir)` tiene cuatro implementaciones: `EstrategiaNormal`, `EstrategiaResbaladiza`, `EstrategiaCinta` y `EstrategiaPortal`.
  - Cada `Terreno` devuelve su estrategia con `getEstrategia()`.
  - El contexto es `Partida`, que al empujar usa la estrategia del terreno al que entra la caja.
  - Los efectos se encadenan con control de ciclos.
- **Justificación:** agregar un terreno con otro recorrido es agregar una estrategia, sin tocar `Partida`. La estrategia se elige por el terreno, no por el país.

### 6.6 Memento - Undo y Reiniciar (diagrama 7)

- **Problema:** Undo tiene que devolver el nivel a un estado anterior aunque se haya roto una caja, tapado un agujero o abierto un muro, sin exponer los atributos privados de `Partida`.
- **Solución y roles:**
  - **Originator:** `Partida`, con `crearMemento()`, `restaurar(m)`, `registrarUsoUndo()` y `reiniciar()`, que restaura el memento inicial.
  - **Memento:** `MementoPartida`, inmutable, con un `EstadoCaja` por caja. Guarda la posición del jugador, cada caja (tipo, posición y resistencia) y los agujeros tapados.
    - Los muros no se guardan porque se recalculan.
    - Sus getters tienen visibilidad de paquete.
  - **Caretaker:** `HistorialPartida`. Guarda hasta 15 mementos, nunca lee su contenido y no conoce a `Partida`.
- **Flujo:**
  - **Mover:** se crea un memento antes de mover y se guarda solo si el movimiento fue válido.
  - **Undo:** `historial.deshacer()` saca hasta 5 mementos y devuelve el más antiguo. Después se llama a `partida.restaurar(m)`.
  - **Reiniciar:** `partida.reiniciar()` e `historial.limpiar()`.

### 6.7 Observer - notificaciones de la partida (diagrama 8)

- **Problema:** después de cada cambio hay que actualizar el tablero y el HUD y reproducir un sonido. Si `Partida` llamara directamente a la vista y al audio, el modelo dependería de Swing.
- **Solución:** `Partida` implementa `PublicadorPartida` (`suscribir`, `desuscribir` y `notificar`). Los observadores implementan `ObservadorPartida.actualizar(EventoPartidaDTO)`.
  - Los observadores concretos son `PresentadorPartida` y `ReaccionAudioPartida`, en el paquete `controlador`.
  - El evento distingue movimiento, Undo, reinicio, victoria y derrota.
- **Justificación:** se puede sumar una reacción nueva sin tocar las reglas, por ejemplo un sistema de logros en el futuro.

### 6.8 Decorator - bonificaciones de puntaje (diagrama 9)

- **Problema:** el puntaje combina una base con bonus y recargos según el nivel. Tener una clase por cada combinación multiplicaría las variantes.
- **Solución:**
  - Componente: la interfaz `CalculadorPuntaje.calcular(resumen)`.
  - Componente concreto: `PuntajeBase`.
  - Decorador base: `DecoradorPuntaje`, que envuelve a otro calculador.
  - Decoradores concretos: `BonusSinUndo`, `BonusPorPar` y `RecargoUndo`.
- **Ejemplo en 4-1:** `BonusPorPar → BonusSinUndo → RecargoUndo → PuntajeBase`. En los demás niveles se omite `RecargoUndo`.

### 6.9 Bridge - solo documentado

Hay una sola vista de juego con el mismo layout en todas las regiones. Sin un editor no existen dos dimensiones de variación, así que se documenta y se incorporaría solo si se agrega un editor (vista de juego vs. vista de edición, por cada región).

## 7. Funcionalidades adicionales

| Funcionalidad | Patrón | Qué agrega |
|---|---|---|
| Regiones temáticas | Abstract Factory | Cada región cambia repartidor, escenario (fondo, pared, piso, destino y muros) y música. Además tiene sus voces y mensajes propios. |
| Terrenos especiales | Strategy | Agujeros, cinta y portales, además del hielo obligatorio |
| Sistema de bonificaciones TeMU | Decorator | Bonus sin Undo, bonus por par y recargo de Undo en 4-1, combinables |

## 8. Principios SOLID

| Principio | Ejemplo concreto en el diseño |
|---|---|
| **S** - Responsabilidad única | `HistorialPartida` solo guarda mementos; `LectorNivel` solo interpreta el TXT; `GestorSonido` solo reproduce audio; la vista solo dibuja DTO. |
| **O** - Abierto/cerrado | Un terreno nuevo se agrega con su clase `Terreno` y su estrategia, sin tocar `Partida`. Un bonus nuevo es otro decorador. Una región nueva es otra fábrica. |
| **L** - Sustitución de Liskov | `Partida` trata cualquier `Caja` o `Terreno` por su interfaz: una `CajaFragil` se empuja igual que una `CajaNormal`. |
| **I** - Segregación de interfaces | Los contratos son chicos y específicos: `ObservadorPartida` tiene un solo método, separado de `PublicadorPartida`, y `CalculadorPuntaje` también tiene uno solo. |
| **D** - Inversión de dependencias | `Partida` depende de `ObservadorPartida` y de `EstrategiaDesplazamientoCaja`, no de las clases concretas. `LectorNivel` depende de `ConstructorNivel` y `CreadorCaja`. |

## 9. Patrones GRASP

| Patrón GRASP | Ejemplo concreto en el diseño |
|---|---|
| **Experto en información** | Cada caja sabe si está entregada (`estaEntregada()`). `CajaFragil` maneja su propia resistencia. `Tablero` conoce las posiciones. |
| **Creador** | `ConstructorNivelSokoban` crea el `Tablero` y sus celdas porque tiene los datos para armarlos. `Partida` crea sus mementos. |
| **Controlador** | `ControladorJuego` recibe los eventos del sistema (flechas, UNDO y REINICIAR) y los delega al modelo. |
| **Bajo acoplamiento** | La vista solo conoce DTO. Observer evita que el modelo conozca Swing. La fachada oculta los subsistemas de carga. |
| **Alta cohesión** | Cada paquete del modelo agrupa una sola cosa: núcleo, creación, estrategias, puntaje u observador. |
| **Polimorfismo** | Terrenos y cajas resuelven su propio comportamiento, sin `switch` por tipo. |
| **Fabricación pura** | `HistorialPartida`, `LectorNivel` y `GestorSonido` no representan algo del dominio; existen para mantener la cohesión. |
| **Indirección** | `PreparadorNivelFacade` media entre el controlador y los subsistemas. `PresentadorPartida` media entre el modelo y la vista. |
| **Variaciones protegidas** | Las interfaces `Terreno`, `Caja`, `EstrategiaDesplazamientoCaja` y `FabricaNivel` protegen al resto del diseño de los cambios en sus implementaciones. |

## 10. Assets

- **Imágenes:** propias del equipo, en pixel art de 16x16.
  - Los tiles de las 6 regiones están basados en los bocetos del grupo.
  - El repartidor viene del diseño de un integrante.
  - Están en `assets/img/`.
- **Audio:** música, efectos y voces en `assets/audio/`. Las voces se prepararon con VoiceStudio.
- **[PENDIENTE]** Completar el origen y la licencia de cada archivo en `ASSETS.md`. La consigna exige dominio público, y que VoiceStudio genere las voces no acredita por sí solo ese requisito.

## 11. Declaración del uso de IA

La consigna permite usar IA como apoyo, siempre que se declare. El equipo usó:

- **Claude (Anthropic), mediante Claude Code:**
  - Revisión y alineación de los diagramas de StarUML con la teoría.
  - Redacción y corrección de los documentos de diseño y de este informe.
  - Generación de los sprites en pixel art a partir de los bocetos del grupo.
  - Conversión del personaje diseñado por el equipo a pixel art.
- **[COMPLETAR]** Otras herramientas usadas: por ejemplo, el borrador inicial del diagrama en PlantUML, que se borra antes de la entrega.
- **VoiceStudio:** generación de las voces regionales.

El equipo revisó, adaptó y validó todo lo generado. Las decisiones de diseño son del equipo, y cada integrante puede explicarlas y defenderlas.

## 12. Conclusiones **[PENDIENTE]**

Completar al terminar:
- Qué se aprendió.
- Qué costó más.
- Qué se haría distinto.
- Qué funcionalidades quedarían para una versión siguiente: editor con Bridge, logros con un observador nuevo y guardado de partida.
