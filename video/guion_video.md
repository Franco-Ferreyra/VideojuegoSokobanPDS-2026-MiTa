# Guion del video de gameplay - TeMU Express

La consigna pide un "video gameplay que muestre todas las funcionalidades implementadas". El orden sigue los niveles de Decisiones (punto 5), así cada mecánica aparece en el nivel donde se estrena.

**Duración estimada:** 6 a 8 minutos.
**Grabación:** pantalla completa de la ventana del juego, con el sonido del juego activado.

## Lista de control (todo tiene que aparecer al menos una vez)

**Tablero**
- [ ] Pared, piso, destino
- [ ] Caja normal, caja frágil (con sus grietas), caja llave
- [ ] Cerrojo, muro cerrado, muro abierto
- [ ] Hielo, agujero, cinta, portales

**Reglas**
- [ ] Jugador bloqueado por una pared, una caja doble o un muro cerrado
- [ ] Victoria
- [ ] Derrota por caja frágil rota

**Interfaz**
- [ ] HUD: nivel, movimientos y empujes
- [ ] Botones UNDO y REINICIAR
- [ ] Indicador de usos de Undo y el "x2" en 4-1
- [ ] Pantalla de resumen
- [ ] 1UP y pantalla final

**Extras**
- [ ] Mensajes regionales
- [ ] Música y voces de cada región
- [ ] Efectos de sonido

---

## 0. Presentación (15 s)
1. Pantalla de inicio con el logo de TeMU Express.
2. Nombre del equipo y de los integrantes.
3. Decir en una frase la idea del juego: un repartidor que lleva paquetes por las aduanas del mundo.

## 1. Nivel 1-1 China (1 min 30 s)
1. Mostrar el mensaje de inicio de China y la música regional.
2. Señalar el HUD:
   - 1UP en 000000
   - NIVEL 1-1 y región CHINA
   - MOVIMIENTOS y EMPUJES en 0
   - indicador de Undo
3. Moverse con las flechas. Mostrar que el contador de movimientos sube y que suena el paso.
4. Intentar atravesar una pared: el jugador no se mueve.
5. Intentar empujar dos cajas en fila: no se puede.
6. Empujar una caja normal hasta un destino. Sube el contador de empujes y suenan el empuje y el destino cumplido.
7. Intentar pasar por un muro cerrado: no se puede.
8. Llevar la caja llave al cerrojo: se abren los muros y suena el desbloqueo.
9. Sacar la caja llave del cerrojo: los muros se vuelven a cerrar.
10. Volver a ponerla y cruzar el muro abierto.
11. Completar el nivel:
    - Mostrar la voz de victoria.
    - Mostrar la **pantalla de resumen** con movimientos, empujes, usos de Undo y puntaje.
    - Mostrar que el **1UP** se actualiza.

## 2. Nivel 2-1 EEUU (1 min 30 s)
1. Mostrar el mensaje de inicio y el cambio de música y de escenario (neón).
2. Empujar la caja frágil y mostrar cómo cambian las grietas: resistencia 4 → 3 → 2 → 1.
3. Empujarla una vez más para que se rompa. Muestra la **derrota**: suena la rotura, aparece el mensaje de derrota y se ofrecen UNDO y REINICIAR.
4. Apretar **UNDO**:
   - La caja vuelve sana, 5 movimientos atrás.
   - Movimientos y empujes no bajan.
   - El indicador muestra un uso menos.
5. Apretar UNDO 2 veces más para mostrar el **máximo de 3 usos seguidos**. La cuarta vez no hace nada.
6. Hacer un movimiento nuevo para mostrar que el indicador de usos se recarga.
7. Apretar **REINICIAR**: el nivel vuelve al estado inicial.
8. Resolver el nivel, usando la llave, el cerrojo y los muros, hasta la pantalla de resumen. Si se usó Undo, mostrar que no se cobra el bonus sin Undo.

## 3. Nivel 3-1 Alemania (1 min)
1. Mostrar el mensaje de inicio y el escenario de arenisca con nieve.
2. Intentar pisar un agujero: el jugador no puede.
3. Empujar una caja al agujero:
   - La caja cae y suena la caída.
   - El agujero queda tapado y pasa a ser piso.
4. Cruzar por el agujero tapado.
5. Mostrar que había una caja de más por cada agujero.
6. Terminar el nivel y mostrar el resumen. Si el movimiento quedó dentro del par, señalar el bonus por par.

## 4. Nivel 4-1 Argentina - Chile (1 min)
1. Mostrar el mensaje de inicio y el indicador de Undo con **x2**.
2. Empujar una caja sobre el **hielo**: patina hasta chocar o salir del hielo y suena el resbalón.
3. Mostrar que el jugador camina por el hielo sin resbalar.
4. Usar UNDO una vez y, al final, mostrar en el resumen el **recargo de Undo** (−100 extra).
5. Usar llave, cerrojo, muros y el agujero. Terminar el nivel.

## 5. Nivel 4-2 Ecuador - Colombia (1 min)
1. Mostrar el mensaje de inicio.
2. Empujar una caja sobre una **cinta**: la caja sigue la flecha de la cinta, no la del empuje. Suena la cinta.
3. Intentar pisar la cinta con el jugador: no se puede.
4. Mostrar de nuevo la caja frágil y el muro junto al cerrojo. Terminar el nivel.

## 6. Nivel 4-3 Perú - Venezuela (1 min 30 s)
1. Mostrar el mensaje de inicio del nivel final.
2. Empujar una caja dentro de un **portal**:
   - La caja sale por el portal del mismo color, en la dirección del empuje.
   - Suena el portal.
3. Mostrar que, si la salida del portal está bloqueada, el empuje no se realiza.
4. Mostrar que el jugador no puede usar los portales.
5. Resolver el nivel combinando cinta, hielo, agujero, caja frágil, llave, cerrojo y muros.
6. Mostrar la pantalla de resumen y después la **pantalla final** con el **1UP** total.

## 7. Cierre (15 s)
- Opcional: mostrar unos segundos la carpeta de niveles con un .txt abierto, para que se vea que los niveles se cargan desde archivos de texto.
- Cerrar con el logo y el nombre del equipo.

---

## Consejos para grabar
- Preparar las soluciones de cada nivel antes de grabar, para no perder tiempo en el video.
- Grabar cada nivel por separado y unirlos después.
- Si una mecánica no se ve clara, agregar un texto corto en la edición (por ejemplo, "Undo: vuelve 5 movimientos").
- Revisar la lista de control antes de exportar.
