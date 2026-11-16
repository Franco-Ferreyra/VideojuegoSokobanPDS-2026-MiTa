# VideojuegoSokobanPDS-2026-MiTa
En este repositorio reside un videojuego estilo Sokoban hecho por alumnos UADE para la materia Proceso de Desarrollo de Software

## TeMU Express (特穆快递)

Sokoban en **Java + Swing (2D)** con estética arcade pixel art. Sos un repartidor de TeMU Express, una parodia de Temu, y tenés que llevar los paquetes a sus destinos a través de las aduanas del mundo. Cada región tiene una traba real que se convierte en una mecánica del juego.

| Nivel | Región | Mecánica principal |
|---|---|---|
| 1-1 | China | Caja llave, cerrojo y muros |
| 2-1 | Estados Unidos | Caja frágil |
| 3-1 | Alemania | Agujeros |
| 4-1 | Argentina - Chile | Terreno resbaladizo (hielo) y Undo con recargo |
| 4-2 | Ecuador - Colombia | Cinta |
| 4-3 | Perú - Venezuela | Portales (nivel final, combina todo) |

### Controles
- **Flechas:** mover al repartidor. Solo empuja cajas, de a una por vez.
- **ENTER (o clic):** pasar los mensajes y las pantallas de resumen.
- **UNDO (clic en el botón):** vuelve 5 movimientos atrás. Se pueden deshacer hasta 15 movimientos, con un máximo de 3 usos seguidos.
- **REINICIAR (clic en el botón):** vuelve el nivel a su estado inicial y pone los contadores en cero.

### Cómo ejecutar
Requisito: **JDK 21**. No hace falta instalar Maven: el repo trae el wrapper (`mvnw`).

1. Compilar y generar el .jar (corre también las pruebas, Spotless y SpotBugs):
   - Windows (PowerShell): `.\mvnw.cmd clean package`
   - Linux / macOS: `./mvnw clean package`
2. Jugar, **desde la raíz del repo** (el juego lee `assets/` y `niveles/` desde ahí):
   ```
   java -jar target/temu-express-0.1.0-SNAPSHOT.jar
   ```

Clase principal: `temuexpress.Aplicacion`. Con un archivo de nivel como argumento abre una versión de consola para probar el motor:
```
java -jar target/temu-express-0.1.0-SNAPSHOT.jar niveles/nivel_1-1.txt
```
Para correr solo las verificaciones: `.\mvnw.cmd clean verify` (JUnit, Spotless con Google Java Format AOSP y SpotBugs).

### Estructura del repositorio
```
├── src/main/java/temuexpress/   Código del juego (MVC)
│   ├── modelo/                  Reglas: núcleo, creación, estrategias, puntaje, observador y DTO
│   ├── vista/                   Pantallas Swing
│   ├── controlador/             Flujo de los niveles y observadores
│   └── recursos/                Fábricas regionales, sonido y fondo animado
├── src/test/                    Pruebas JUnit
├── assets/                      Imágenes y audio del juego (ver ASSETS.md)
├── niveles/                     Los 6 niveles en TXT y sus soluciones verificadas
├── docs/                        Documentación del TP (índice en docs/README.md)
├── ASSETS.md                    Origen y licencia de cada asset
└── pom.xml, mvnw, mvnw.cmd      Build con Maven
```

### Documentación
- `docs/informe/informe_tecnico.md`: informe técnico.
- `docs/diagramas/TeMUExpress.mdj`: diagramas de clases en StarUML. Los PNG están en `docs/diagramas/png/`.
- `docs/diseno/`: decisiones del equipo (`Decisiones_MVP_TeMU_ExpressFinal.txt`, que prevalecen sobre el resto), diseño de niveles y patrones.
- `docs/motor/`: funcionamiento del motor y formato de los niveles.
- `docs/textos/mensajes_regionales.md`: mensajes de cada nivel y personajes.
- `docs/video/guion_video.md`: guion del video de gameplay.
- `docs/bocetos/`: bocetos de cada nivel.
- `niveles/SOLUCIONES.md`: solución verificada de cada nivel y reglas de las mecánicas.

### Assets
Todos los assets son del equipo y se liberan como dominio público (CC0). Las imágenes son pixel art propio (`assets/img/`). El audio (`assets/audio/`) lo hizo un integrante con su propia voz, procesada en VoiceStudio. El detalle de cada archivo está en `ASSETS.md`.

### Equipo: Los DesubiAmericanos
- Ferreyra, Franco Elian - 1190726
- Obregon, Santiago - 1211437
- Diaz, Matias - 1209251
- Gonzalez Jimenez, Alejandra - 1198460
- Quijada, Jesús - 1195298
- Rodrigo, Juan Manuel - 1223897

Materia: Proceso de Desarrollo de Software (UADE) - Prof. Jonathan Leonel Pepe.
