# Mensajes regionales - TeMU Express (特穆快递)

Equipo: Los DesubiAmericanos

Textos que muestra el juego en cada nivel. Están en `src/main/java/temuexpress/controlador/MensajesRegionales.java`; este archivo es la referencia para revisarlos y cambiarlos.

Cada nivel tiene:
- **Mensaje de inicio en 2 páginas:** la historia del repartidor y la misión. La misión cuenta el "problema real" que sufre un paquete de TeMU en esa región y explica la mecánica (Diseno_Niveles_TeMU_Express_v2.txt).
- **Entregas:** lo que dice el repartidor en el globo de diálogo al dejar una caja en un destino. Las frases van rotando.
- **Caídas:** lo que dice cuando una caja cae a un agujero (solo en los niveles con agujeros).
- **Derrota:** una frase del repartidor (va rotando) y lo que pasó. La derrota aparece cuando se rompe una caja frágil o cae una caja llave en un agujero.

Al terminar el 4-3 hay un cierre de 2 páginas: qué fue de cada repartidor y el puntaje total.

---

## 1-1 China (HUD: CHINA)

**Mecánicas:** cajas normales, caja llave, cerrojo y muros.

**Página 1 - XI DADA**
> Xi Dada es el repartidor más antiguo de TeMU Express.
> Empezó en un depósito de Shenzhen armando cajas de a millones, sin un solo día libre.
> Dicen que nunca perdió un paquete... porque nunca lo dejaron salir del depósito.
> Hoy, por fin, le toca su primer envío al exterior.

**Página 2 - CHINA**
> La aduana bloqueó tu compra y pide tu documento para todo.
> Llevá la CAJA LLAVE hasta el CERROJO para abrir los muros.
> Si la sacás del cerrojo, te retienen el documento otra vez.

**Entregas:** "¡Hǎo! (¡Bien!) Paquete entregado." / "Xièxie, cliente. Vuelva pronto." / "Otro envío de Shenzhen. Quedan mil millones."

**Derrota:** "Trámite rechazado."
- "Paquete confiscado. Ahora son 40 formularios más."
- "Ni en Shenzhen me pasaba esto. Volvamos a intentar."
- "La aduana gana esta vez. La próxima, no."

---

## 2-1 Estados Unidos (HUD: EEUU)

**Mecánicas:** caja frágil, caja llave, cerrojo y muros.

**Página 1 - DONALD "DONNY" TRUMP**
> Donny empezó de abajo: con un pequeño préstamo de su papá.
> El préstamo era una empresa de paquetería entera.
> Un día preguntó: "¿Y los paquetes cómo llegan?". Silencio total.
> Hoy reparte él mismo. Primer trabajo de su vida. Tremendo.

**Página 2 - EEUU**
> Control sorpresa: los inspectores federales revisan todo de más.
> La CAJA FRÁGIL aguanta pocos empujes; mirá sus grietas.
> Al cuarto empujón se rompe y perdés el envío.

**Entregas:** "Entregado. Nadie entrega como yo, todos lo dicen." / "Una entrega enorme. Histórica. La mejor." / "Pónganle mi nombre a esa caja. En dorado."

**Derrota:** "Tu paquete no sobrevivió a la inspección."
- "¿Rota? Fake news. Esa caja estaba perfecta."
- "Uy, se rompió esta caja. Mejor le mando un cheque de 10 mil dólares."
- "Uyyy, se rompió esta caja. Papá, llama a los abogados, ¡pero rápido!"

---

## 3-1 Alemania (HUD: ALEMANIA)

**Mecánicas:** agujeros y caja frágil.

**Página 1 - DER FÜHRER**
> Antes de ser el malo de esta historia, era un joven pintor que vivía en Viena.
> Pintaba paisajes y edificios todo el día; le gustaba pintar demasiado.
> Reparte paquetes de TeMU para juntar plata y entrar a la gran Academia de Bellas Artes.
> Cada caja que entrega es una clase de pintura más. No le falles.

**Página 2 - ALEMANIA**
> Los impuestos de importación cuestan más que el producto.
> Empujá una caja a cada AGUJERO para taparlo: son los aranceles.
> Trajiste una caja de más por cada agujero. Elegí bien cuál sacrificás.

**Entregas:** "Sehr gut! Una obra maestra." / "Wunderbar, esto merece un marco." / "Danke! Una clase de pintura más."

**Caídas:**
- "Ach du lieber! ¡Ay, Dios mío! Al pozo el paquete se fue, ja."
- "Tschüss, Paket! ¡Chau, paquete! Muy bien caído, ja."
- "Ordnung muss sein! Orden ante todo: un agujero, una caja, ja."
- "Wunderbar! ¡Maravilloso! El arancel pagado está, ja."

**Derrota:** "Se perdió la caja equivocada."
- "Nein, nein, nein. Me arruinaste el cuadro."
- "Rechazado otra vez... como en la escuela de arte."
- "Sin esta entrega no llego a pagar la Academia."

---

## 4-1 Argentina - Chile (HUD: ARG-CHILE)

**Mecánicas:** terreno resbaladizo (hielo), cerrojo y muros, agujero y recargo de Undo.

**Página 1 - SIMONCITO**
> Simoncito llegó a Sudamérica con un sueño: unir todo el continente con envíos.
> Le dicen así desde chico, por su abuelo, que cruzó los Andes a caballo con cartas bastante subidas de tono para su abuela.
> Ahora él los cruza con paquetes de TeMU y un termo de mate bajo el brazo.

**Página 2 - ARG-CHILE**
> Cupos de compra y papeleo: cualquier error te sale carísimo.
> En el HIELO, la caja patina hasta chocar o salir del hielo.
> Acá, cada UNDO cuesta el doble en el puntaje (x2).

**Entregas:** "¡Uno menos, che!" / "¡Bacán, llegó al toque!" / "¡Joya, entregado antes del cepo!"

**Caídas:**
- "¡Se fue al pozo, che! Ni lo vi."
- "¡Chuta, se lo tragó la tierra!"
- "¿Cachai? Un hoyo menos."
- "¡Bajó más rápido que el peso!"

**Derrota:** "Error en el formulario."
- "Uh, se congeló el trámite. Hay que arrancar de nuevo."
- "Ni el mate me salva de esta cordillera de papeles."
- "Se patinó todo, che. Volvamos a probar."

---

## 4-2 Ecuador - Colombia (HUD: ECU-COL)

**Mecánicas:** cinta, caja frágil, cerrojo y muros.

**Página 1 - SIMONCITO**
> Simoncito sigue viaje hacia el norte, entre Ecuador y Colombia.
> En cada pueblo le invitan un café y le preguntan por qué tarda tanto el correo.
> Su lema: ningún paquete queda atrás, aunque se lo lleve la cinta.

**Página 2 - ECU-COL**
> La aduana retiene tu paquete y lo pasa por el escáner.
> La CINTA arrastra la caja hacia donde marca la flecha.
> Vos no podés pisarla: solo mirás cómo se lo llevan.

**Entregas:** "¡Qué chévere, parcero!" / "¡Listo, mijo, paquete liberado!" / "¡Bacano, uno menos pa'l escáner!"

**Derrota:** "El paquete quedó retenido en la inspección."
- "Se lo llevó la cinta. Ni el escáner lo encuentra."
- "Retenido otra vez. Me voy a tomar otro café."
- "Ese paquete no pasó el control. Probemos de nuevo."

---

## 4-3 Perú - Venezuela (HUD: PERU-VEN) - nivel final

**Mecánicas:** portales, cinta, hielo, agujero, caja frágil, caja llave, cerrojo y muros.

**Página 1 - SIMONCITO**
> Llegó la última aduana. Simoncito recorrió medio mundo por este paquete.
> Xi Dada lo despachó, Donny lo inspeccionó y el pintor de Alemania lo cruzó por Europa.
> Si lo entrega, TeMU Express lo nombra Repartidor del Año.

**Página 2 - PERU-VEN**
> El correo no llega: el paquete viaja por casilleros privados.
> Cada PORTAL lleva la caja al otro portal del mismo color.
> Es la última aduana: todo lo que aprendiste junto.

**Entregas:** "¡Llegó, causa! Está bravazo." / "¡Chévere, pana!" / "¡Épale, otro casillero cumplido!"

**Caídas:**
- "¡Asu mare, se lo tragó el hueco!"
- "¡Qué chimbo, pana, se fue pa'l hueco!"
- "¡Chamo, ese casillero no tenía fondo!"
- "¡Habla, causa! Un hueco menos en la ruta."

**Derrota:** "El casillero falló."
- "El casillero se tragó el paquete. Otra ruta, causa."
- "Tan cerca del final... Vamos de nuevo."
- "Ni con portales llega este correo. Otra vez."

---

## Cierre del juego (después de 4-3)

**Página 1 - LOS REPARTIDORES**
> Xi Dada se tomó su primer día libre en 40 años. Lo pasó armando cajas.
> Donny compró a la competencia. Dice que repartir es fácil, el más fácil.
> El pintor juntó la plata para la Academia. El examen... mejor ni hablar.
> Simoncito es Repartidor del Año. La abuela todavía guarda las cartas.

**Página 2 - Final**
> TeMU Express llevó el paquete de China a Sudamérica.
> Puntaje total (1UP): [puntaje]
> Gracias por elegir TeMU Express.

---

## Personajes

Base: el repartidor de TeMU que diseñó el grupo, pasado a pixel art en `assets/img/personaje/` en 4 direcciones. En el diagrama 4 cada fábrica regional crea su `AspectoRepartidor`, así que hay un repartidor por familia, cada uno con su color.

| Familia | Nombre | De dónde sale | Sprite |
|---|---|---|---|
| China | **Xi Dada** ("Tío Xi" o "Gran Papá Xi") | Apodo del presidente de China. | `repartidor_azul` |
| EEUU | **Donny Trump** | Apodo de Donald Trump. | `repartidor_amarillo` |
| Alemania | **Der Führer** | Seudónimo o título de Adolfo Hitler. | `repartidor_rojo` |
| Sudamérica | **Simoncito** o **El negro de América** | Apodos de Simón Bolívar. | `repartidor_verde` |

**Esqueleto:** se muestra en la pantalla de derrota, cuando se rompe una caja frágil o cae una caja llave en un agujero. Cada repartidor tiene su esqueleto con sus colores (`repartidor_<color>/esqueleto.png`).
