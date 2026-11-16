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

### Documentación
- `Decisiones_MVP_TeMU_ExpressFinal.txt`: decisiones cerradas del equipo, que prevalecen sobre el resto.
- `Patrones_*_TeMU_Express.txt`: patrones creacionales, estructurales y de comportamiento.
- `diagramas/TeMUExpress.mdj`: diagramas de clases en StarUML. Los PNG están en `diagramas/png/`.
- `informe/informe_tecnico.md`: informe técnico.
- `textos/mensajes_regionales.md`: mensajes de cada nivel y personajes.
- `video/guion_video.md`: guion del video de gameplay.
- `docs/MOTOR_BASICO.md` y `docs/FORMATO_NIVELES.md`: motor y formato de los niveles.
- `niveles/SOLUCIONES.md`: solución verificada de cada nivel y reglas de las mecánicas.

### Assets
Las imágenes son propias del equipo, en pixel art (`assets/img/`). El audio está en `assets/audio/`. El origen y la licencia de cada asset se registran en `ASSETS.md` **[PENDIENTE]**.

### Equipo: Los DesubiAmericanos
- [Apellido, Nombre]
- [Apellido, Nombre]
- [Apellido, Nombre]
- [Apellido, Nombre]
- [Apellido, Nombre]

Materia: Proceso de Desarrollo de Software (UADE) - Prof. Jonathan Leonel Pepe.
