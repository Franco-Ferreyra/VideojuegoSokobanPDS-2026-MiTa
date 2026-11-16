# Assets - TeMU Express (特穆快递)

Origen y licencia de cada imagen y sonido del juego. La consigna pide que los assets sean de dominio público: por eso solo se usan recursos hechos por el equipo o con licencia CC0 (Decisiones_MVP_TeMU_ExpressFinal.txt, punto de assets).

No se usa ningún logo, imagen ni sonido de la marca Temu: el nombre y el logo de "TeMU Express" son una parodia hecha por el equipo.

## Imágenes (`assets/img/`)

Todas son propias del equipo, en pixel art. Se liberan como dominio público (CC0).

| Archivos | Qué son | Origen |
|---|---|---|
| `<región>/` (28 por región: `china`, `usa`, `alemania`, `arg_chile`, `ecuador_colombia`, `peru_venezuela`) | Tiles de 16x16 (pared, piso, destino, muros, cajas, cerrojo, agujero, hielo, cintas y portales) | Dibujados a partir de los bocetos del grupo (`bocetos/`). Los sprites se generaron con scripts de Python (Pillow) hechos por el equipo con asistencia de IA. |
| `<región>/fondo.png` | Fondo de cada región con el HUD dibujado | Bocetos del grupo, normalizados a 432x324. En Perú - Venezuela se sacaron los helicópteros del dibujo porque ahora son sprites animados. |
| `<región>/anim/` | Sprites del fondo animado (nubes, farolitos, avioneta, zepelín, avión, loros, helicópteros, cóndor) | Generados por el equipo con un script de Python (Pillow). |
| `personaje/repartidor_{azul,amarillo,rojo,verde}/` | Repartidor en 4 direcciones y su esqueleto | Diseño de un integrante del grupo, pasado a pixel art. |
| `logo.png` | Logo de TeMU Express | Hecho por el equipo. |

Los efectos de la pantalla (cielo que cambia de color, nieve, cascada, luces de Las Vegas y reflectores) no son imágenes: los dibuja el código (`temuexpress.recursos.Capa*`).

## Audio (`assets/audio/`)

Todo el audio del juego fue producido por un integrante del equipo con la herramienta de código abierto VoiceStudio (https://github.com/debpalash/VoiceStudio). La única fuente sonora fue la voz del propio integrante: a partir de sus grabaciones se generaron las voces de los personajes, los efectos de sonido y la música. Todos los archivos están en formato WAV mono a 44,1 kHz.

| Carpeta | Archivos | Descripción |
|---|---|---|
| `voices/<región>/` | `inicio`, `empuje`, `desbloqueo`, `undo`, `victoria`, `derrota` (6 por región, 36 en total) | Voces de los personajes, adaptadas al acento y al estilo de cada región. Los textos fueron escritos por el equipo. |
| `sfx/` | `agujero_cae`, `boton_click`, `cerrojo_activa`, `cinta`, `derrota`, `empuje`, `fragil_dano`, `fragil_rompe`, `meta_cumplida`, `muro_abre`, `muro_cierra`, `paso`, `portal`, `reiniciar`, `resbalar`, `undo`, `victoria` (17) | Efectos de sonido de cada acción y mecánica, generados a partir de la voz del integrante. |
| `music/` | `musica_china`, `musica_eeuu`, `musica_alemania`, `musica_sudamerica` (4) | Música en loop de cada familia de niveles, generada a partir de la voz del integrante. |

**Justificación del dominio público:**
- **Material de origen propio.** Todo el audio se generó a partir de la voz de un integrante del equipo, que autorizó su uso. No se incorporaron grabaciones, samples, canciones ni voces de terceros.
- **Sin imitación de personas reales.** Las voces de los personajes no se clonaron de ninguna persona ajena al equipo; son interpretaciones de la voz del integrante.
- **Uso de la herramienta.** VoiceStudio se utilizó como herramienta de procesamiento y síntesis sobre el material propio, de la misma forma que un editor de audio. El juego solo reproduce los archivos WAV y no depende de VoiceStudio para funcionar.
- **Autoría del equipo.** Al ser una obra del equipo, el audio se libera como dominio público (CC0), con el mismo criterio que las imágenes.

## Tipografía

El juego usa la fuente sans serif del sistema (`Font.SANS_SERIF` de Java). No se distribuye ningún archivo de fuente.
