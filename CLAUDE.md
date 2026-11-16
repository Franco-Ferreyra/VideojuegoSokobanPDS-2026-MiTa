# CLAUDE.md — TeMU Express (TP PDS UADE 2026)

Contexto para que cualquier chat nuevo retome el TP sin re-preguntar.
Actualizado: 16/11/2026.

## 1. Qué es

- TP de Proceso de Desarrollo de Software (UADE). Equipo "Los DesubiAmericanos". Profesor: Jonathan Pepe (jpepe@uade.edu.ar).
- Juego tipo Sokoban en **Java 21 + Swing**, temática "TeMU Express": un repartidor lleva paquetes (cajas) a destinos en 4 regiones (China, EEUU, Alemania, Sudamérica con 3 variantes).
- Repo: github.com/Franco-Ferreyra/VideojuegoSokobanPDS-2026-MiTa (público).

## 2. Cómo trabajar con el usuario (OBLIGATORIO)

- Hablar en **español rioplatense**, respuestas **cortas y simples**.
- **No subir nada sin autorización.** Antes de cualquier commit/push: mostrar lo que se hizo (archivo completo / resultado) y esperar el "sí".
- **No hacer nada de más.** Solo lo pedido. Consultar antes de crear cosas nuevas.
- **Se trabaja directo sobre `main`** (sin ramas ni PRs). Hacer `git pull` antes de empezar y antes de commitear, para no pisar lo de los compañeros; todo tiene que quedar alineado con lo que hicieron ellos.
- Antes de subir, correr `.\mvnw.cmd clean verify` y que pase todo.
- `Teoria/` nunca se sube a git (hoy está vacía localmente).
- Descargar archivos requiere permiso explícito. Assets solo propios o CC0.
- **StarUML:** una sola ventana. Nunca usar File > Open Recent (abre otra ventana; para recargar, cerrar primero). No usar `C:\Users\Jesus\Downloads\Strategy.mfj` ni guardar el archivo del compañero.
- En cada diagrama, revisar clases sueltas y tipos sin relación antes de presentarlo.
- Principios de diseño a respetar (Shvets, "Dive Into Design Patterns", en `C:\Users\Jesus\Desktop\BuenasPracticas`): SOLID, Encapsulate What Varies, Program to an Interface, Composition over Inheritance.

## 3. Documentos y prioridad

1. **`docs/diseno/Decisiones_MVP_TeMU_ExpressFinal.txt`** — manda sobre todo. Tiene la sección PENDIENTES (Diseño / Assets / Niveles / Código / Entrega). Archivo con CRLF: preservarlo.
2. **`docs/diagramas/TeMUExpress.mdj`** (StarUML) — los nombres de clases y métodos salen de acá. 11 diagramas (0 a 10); PNGs exportados en `docs/diagramas/png/`.
3. `docs/motor/MOTOR_BASICO.md`, `docs/motor/FORMATO_NIVELES.md` — motor y formato de niveles.
4. `niveles/SOLUCIONES.md` — soluciones y reglas de mecánicas.
5. Otros: `README.md`, `ASSETS.md` (origen y licencia de los assets), `docs/informe/informe_tecnico.md`, `docs/video/guion_video.md`, `docs/textos/mensajes_regionales.md`, `docs/diseno/` (`Patrones_*.txt`, `Resumen_TP*` resumen de la consigna, `Diseno_Niveles_v2*`), `docs/bocetos/` (uno por nivel). Índice en `docs/README.md`.
6. Raíz: solo `README.md`, `ASSETS.md`, `CLAUDE.md`, build Maven, `assets/` y `niveles/` (el juego los lee desde la raíz: no moverlos) y `src/`.

Exportar diagramas desde CLI:
```
"C:\Program Files\StarUML\StarUML.exe" image docs/diagramas/TeMUExpress.mdj -f png -s "@UMLClassDiagram" -o "docs/diagramas/png/<%=element.name%>.png"
```

## 4. Build, tests y ejecución

- Maven wrapper + Java 21. Usar **PowerShell** (Git Bash rompe el classpath con `;`). Correr Maven desde la raíz del repo.
- `.\mvnw.cmd clean verify` → JUnit (37 tests), Spotless (Google Java Format AOSP), SpotBugs (max effort, threshold Low, sin exclusiones).
- Formatear: `.\mvnw.cmd spotless:apply`.
- Juego completo (desde la raíz del repo, lee `assets/` y `niveles/`): `java -jar target/temu-express-0.1.0-SNAPSHOT.jar`. Clase principal `temuexpress.Aplicacion`.
- Consola (probar el motor): `java -jar target/temu-express-0.1.0-SNAPSHOT.jar niveles/nivel_1-1.txt`.
- Ojo SpotBugs: en clases Swing serializables usar campos `transient` + `serialVersionUID`; `toUpperCase/LowerCase(Locale.ROOT)`; usar `SplittableRandom` en vez de `Random`.

## 5. Arquitectura (MVC)

Paquetes en `src/main/java/temuexpress/`:
- `modelo.nucleo` (Partida, Tablero, Celda, terrenos, cajas, Memento), `modelo.creacion` (LectorNivel + ConstructorNivelSokoban — Builder; CreadorCaja* — Factory Method; PreparadorNivelFacade — Facade), `modelo.estrategias` (Strategy + Encadenamiento), `modelo.observador` (Observer), `modelo.puntaje` (Decorator), `modelo.dto`.
  - El back empezó siendo de los compañeros; las mecánicas y los patrones los terminamos nosotros (commit 1e306c4). Avisarles que hagan `git pull`.
  - DTOs: `TableroDTO(int ancho, int alto, String terreno, String objetos)` (capas = filas unidas por `\n`, con símbolos del TXT; la caja frágil viaja como `1`–`4` según su resistencia); `EventoPartidaDTO(tipo, nivel, movimientos, empujes, usosUndoSeguidos)`; `ResumenNivelDTO(movimientos, empujes, usosUndo, par)`.
- `controlador` — `ControladorJuego` (implementa OyenteVista): flujo de los 6 niveles (inicio, mensaje, juego, resumen, final), Undo/Reiniciar con Memento, puntaje con Decorator. `MensajesRegionales` (textos), `PresentadorPartida` y `ReaccionAudioPartida` (observadores).
- `recursos` — Abstract Factory (`FabricaNivel` + 4 fábricas; productos AspectoRepartidor, Escenario y SonidoRegional, con clases base AspectoRepartidorColor, EscenarioCarpeta y SonidoArchivos), `GestorSonido` (Singleton enum, hilo propio, clips precargados, voz y efecto por prioridad), `Imagenes`, y el fondo animado (`AnimacionFondo`, `FondoAnimado`, `Capa` + 8 capas, `Cuadro`, `Sprite`).
- `vista` — front:
  - `VentanaJuego` (JFrame por composición, implementa VistaPartida). API: `mostrar`, `setOyente`, `setSprites`, `setAnimacionFondo`, `setPuntajeTotal`, `mostrarInicio`, `mostrarMensaje(titulo, String[], pie)`, `mostrarDialogo`, `mostrarJuego(TableroDTO)`, `actualizar(TableroDTO, EventoPartidaDTO)`, `mostrarResumen(ResumenNivelDTO, int)`, `mostrarDerrota(String[])`, `mostrarFinal(String[])`.
  - Interfaz `Pantalla`: PantallaInicio (logo flotando con brillo), PantallaMensaje, PantallaJuego, PantallaResumen, PantallaDerrota, PantallaFinal. Más: PanelJuego (Timer de 15 cuadros/s para el fondo), VistaTablero, VistaHUD, EstiloRegion, HojaSprites, Cartel (se ajusta al texto), Texto, Disposicion, OyenteVista, VistaPartida.
  - Resolución base 432x324, escala x2. Tablero 13x7 en (112,188), tiles de 16 px. El `fondo.png` de cada región es el boceto normalizado con el HUD dibujado; VistaHUD escribe los valores encima. EstiloRegion toma colores del fondo.

### Claves de HojaSprites
- Terreno: `pared, piso, destino, cerrojo, muro_cerrado, muro_abierto, agujero, hielo, cinta_arriba/abajo/izquierda/derecha, portal_1..9`
- Objetos: `caja_normal, caja_llave, caja_fragil_1..4, jugador_arriba/abajo/izquierda/derecha, esqueleto`
- Pantalla: `fondo, logo`
- Si falta una imagen, HojaSprites dibuja un reemplazo provisorio.

### Assets (`assets/img/`)
- Una carpeta por región: `china, usa, alemania, arg_chile, ecuador_colombia, peru_venezuela`. Cada una: 11 tiles base + `fondo.png` + 16 sprites de mecánicas (iguales en todas las regiones, como las cajas) + `anim/` con los sprites del fondo animado.
- `logo.png`.
- `personaje/repartidor_{azul,amarillo,rojo,verde}/` con `arriba, abajo, izquierda, derecha, esqueleto`. Azul=China, Amarillo=EEUU, Rojo=Alemania, Verde=Sudamérica.
- Audio en `assets/audio/` (música por familia, efectos y voces por región).

## 6. Niveles

Formato (`docs/motor/FORMATO_NIVELES.md`): cabeceras `REGION=`, `VARIANTE=`, `PAR=`, luego `[TERRENO]` y `[OBJETOS]`. Máximo 13x7, cada espacio cuenta. `.gitattributes`: `/niveles/*.txt -text`.
- Terreno: `#` pared, `.` destino, `L` cerrojo, `W` muro, `O` agujero, `~` hielo, `> < ^ v` cintas, `1-9` portales (de a pares).
- Objetos: `@` jugador, `$` caja normal, `F` caja frágil, `K` caja llave.

`niveles/nivel_*.txt` (6 mapas, verificados con solver):

| Nivel | Región / variante | Mecánicas | Movs | Empujes | PAR |
|---|---|---|---|---|---|
| 1-1 | CHINA | cerrojo/muro, llave | 28 | 8 | 34 |
| 2-1 | EEUU | + caja frágil | 21 | 8 | 26 |
| 3-1 | ALEMANIA | agujeros, frágil | 38 | 21 | 46 |
| 4-1 | SUDAMERICA / ARG-CHILE | hielo, agujero, muros | 58 | 22 | 70 |
| 4-2 | SUDAMERICA / ECU-COL | cinta, frágil, muro | 51 | 22 | 62 |
| 4-3 | SUDAMERICA / PERU-VEN | portal, cinta, hielo, todo | 79 | 29 | 95 |

PAR = solución × 1.2 redondeado arriba. 1-1 a 4-2 son óptimos en movimientos; 4-3 es solo una solución verificada. El motor los carga y los juega con todas las mecánicas.

### Reglas de mecánicas (las usadas para verificar los mapas, ver final de SOLUCIONES.md)
- **Hielo:** la caja sigue en la dirección del empuje y aplica el efecto de la celda siguiente. El jugador puede caminar sobre hielo.
- **Cinta:** la caja sigue la dirección de la cinta; si está bloqueada queda sobre la cinta.
- **Portal:** la caja sale por el portal par en la dirección de entrada; si la salida está bloqueada, el empuje se rechaza.
- **Agujero:** caja normal o frágil cae, desaparece y el agujero pasa a piso. Si cae una caja llave, el nivel queda imposible (derrota).
- **Frágil:** resistencia 4, -1 por empuje (un deslizamiento cuenta como un empuje); al 4º empuje se rompe.
- **Muros:** abiertos sii todos los cerrojos tienen una caja llave encima. Para la caja empujada, el estado se calcula sin contarla.
- El jugador no pisa agujeros abiertos, cintas ni portales.

## 7. Estado del repo (16/11/2026)

- Se trabaja directo sobre `main`. El juego está completo: 6 niveles, todas las mecánicas y patrones, audio y fondos animados (commit 1e306c4).
- Diagramas UML alineados con el código, incluido el nuevo "10 - Fondo animado" (commit 13c8a43).
- La demo temporal (`demo.DemoSwing`) ya se borró.

## 8. Qué falta para entregar

- Grabar el video (guion en `docs/video/guion_video.md`).
- Mandar el mail a jpepe@uade.edu.ar.
- Ver la sección PENDIENTES de `docs/diseno/Decisiones_MVP_TeMU_ExpressFinal.txt`.

## 9. Herramientas auxiliares (fuera del repo, pueden no existir en otra sesión)

En el scratchpad de la sesión anterior (`C:\Users\Jesus\AppData\Local\Temp\claude\8f8a3f56c77754f4\1a2a9556-9af0-4f5b-9688-b49730b4b56a\scratchpad\`):
- `mapas/solver.py` — validador + BFS por empujes (`resolver`, `resolver_movs`, `mecanicas`) con las reglas de la sección 6.
- `mapas/generar.py` — genera los TXT desde una grilla combinada.
- `mecanicas.py` — generador (Pillow) de los sprites de mecánicas.

En el scratchpad de la sesión del 16/11 (`C:\Users\Jesus\AppData\Local\Temp\claude\8f8a3f56c77754f4\e731adf6-b8e2-49fa-9cf5-b7032567b055\scratchpad\`):
- `uml/mdj.py` — helpers para editar el `.mdj` sin abrir StarUML (agregar clases, métodos, vistas y flechas clonando las existentes). Usar con StarUML cerrado.
- `cielo/sprites_anim.py` — generador de los sprites de `anim/`. Prototipos de los fondos en `C:\Users\Jesus\Desktop\JAVA--PROGRA2\prototipo_cielo_animado\`.
