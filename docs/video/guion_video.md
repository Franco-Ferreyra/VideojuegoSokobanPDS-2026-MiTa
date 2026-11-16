# Guion del video de gameplay - TeMU Express

La consigna pide un "video gameplay que muestre todas las funcionalidades implementadas". El orden sigue los niveles de Decisiones (punto 5), así cada mecánica aparece en el nivel donde se estrena.

**Video:** `TeMU_Express_gameplay.mp4` (en esta carpeta), 3:59.
**Grabación:** el gameplay se grabó de corrido y sin cortes con Xbox Game Bar, con el sonido del juego. Las jugadas son las soluciones verificadas de `niveles/SOLUCIONES.md`. La presentación y el cierre son animaciones con los mismos fondos, sprites y música del juego, unidas al gameplay con ffmpeg.

## Lista de control (todo tiene que aparecer al menos una vez)

**Tablero**
- [ ] Pared, piso, destino
- [ ] Caja normal, caja frágil (con sus grietas), caja llave
- [ ] Cerrojo, muro cerrado, muro abierto
- [ ] Hielo, agujero, cinta, portales

**Reglas**
- [ ] Jugador bloqueado por una pared, dos cajas seguidas o un muro cerrado
- [ ] Victoria
- [ ] Derrota por caja frágil rota

**Interfaz**
- [ ] HUD: nivel, movimientos y empujes
- [ ] Botones UNDO y REINICIAR
- [ ] Indicador de usos de Undo y el "x2" en 4-1
- [ ] Pantalla de resumen
- [ ] 1UP, cierre de los repartidores y pantalla final

**Extras**
- [ ] Portada animada y fondo animado de cada región
- [ ] Historia del repartidor y misión de cada nivel
- [ ] Frases del repartidor al entregar, al caer una caja y al perder
- [ ] Música, voces y efectos de sonido de cada región

---

## Presentación (24 s)
1. China: "Proceso de Desarrollo de Software", UADE 2026 y el profesor Jonathan Leonel Pepe.
2. EEUU: "Los DesubiAmericanos presentan", con los cuatro repartidores.
3. Alemania: el equipo. Cada repartidor empuja una caja y aparece un integrante con su legajo.
4. Perú - Venezuela: cae el logo de TeMU Express con su brillo.

## 0. Portada (3 s)
- Logo flotando con su brillo, el equipo y "ENTER para empezar" titilando.

## 1. Nivel 1-1 China
1. Mensaje de inicio en dos páginas: la historia de Xi Dada y la misión. Suenan la voz y la música de China.
2. Tablero con el cielo animado. HUD: 1UP en 000000, NIVEL 1-1, movimientos y empujes en 0, indicador de Undo.
3. Intentar bajar contra la pared: el jugador no se mueve.
4. Caminar hasta el muro e intentar cruzarlo: está cerrado.
5. **REINICIAR**: el nivel vuelve al inicio y los contadores a 0.
6. Llevar la caja llave al cerrojo: se abren los muros y suena el desbloqueo.
7. Resolver el nivel: entregas con las frases de Xi Dada, voz de victoria, **resumen** y **1UP** actualizado.

## 2. Nivel 2-1 EEUU
1. Mensaje de inicio (historia de Donny) y cambio de música y escenario (reflectores y luces de Las Vegas).
2. Abrir el muro con la llave y empujar la caja frágil: las grietas pasan de 4 a 1.
3. Un empuje más la rompe: **derrota**, con el esqueleto y la frase de Donny.
4. **UNDO**: vuelve 5 movimientos, la caja está sana, los movimientos y empujes no bajan y el indicador muestra un uso menos.
5. UNDO dos veces más (máximo de 3 seguidos) y una cuarta vez, que no hace nada.
6. Un movimiento nuevo recarga el indicador.
7. REINICIAR y resolver el nivel hasta el resumen.

## 3. Nivel 3-1 Alemania
1. Mensaje de inicio (historia del pintor) y atardecer con zepelín.
2. Intentar empujar dos cajas seguidas: no se puede.
3. Empujar cajas a los agujeros: caen, suena la caída, el pintor comenta y el agujero queda tapado.
4. Llevar la caja frágil a su destino y terminar el nivel. Resumen con el bonus por PAR.

## 4. Nivel 4-1 Argentina - Chile
1. Mensaje de inicio (historia de Simoncito), nieve cayendo e indicador de Undo con **x2**.
2. Empujar cajas sobre el **hielo**: patinan hasta chocar. El jugador camina sobre el hielo sin resbalar.
3. Usar UNDO una vez y repetir las jugadas. En el resumen se ve el **recargo de Undo**.
4. Llave, cerrojo, dos muros y un agujero. Terminar el nivel.

## 5. Nivel 4-2 Ecuador - Colombia
1. Mensaje de inicio y loros en el cielo.
2. Empujar cajas sobre la **cinta**: siguen la flecha de la cinta y cruzan la pared. El jugador no puede pisarla.
3. Caja frágil, caja llave, cerrojo y muro. Terminar el nivel.

## 6. Nivel 4-3 Perú - Venezuela
1. Mensaje de inicio del nivel final. Cascada del Salto Ángel, helicópteros y cóndor.
2. El hielo lleva una caja al **portal** y sale por el portal del mismo color, en la misma dirección. La cinta la baja al destino.
3. Resolver el nivel combinando todo: hielo, portal, cinta, agujero, caja frágil, llave, cerrojo y muro.
4. Resumen, cierre **LOS REPARTIDORES** y **pantalla final** con el 1UP total.

## Cierre (10 s)
- "¡Gracias por ver!" con el equipo, la materia y el profesor. Los cuatro repartidores entregan sus cajas y festejan; "FIN DEL REPARTO".

---

## Notas
- La grabación es automática: un script juega con las soluciones verificadas y deja cada pantalla el tiempo justo para leerla.
- La ventana se agrandó para que el juego quede centrado con un marco negro alrededor (escala x2, sin perder nitidez).
