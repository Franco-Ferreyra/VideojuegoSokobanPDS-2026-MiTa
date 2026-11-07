# Mensajes regionales - TeMU Express (特穆快递)

Equipo: Los DesubiAmericanos

Cada nivel muestra un mensaje al iniciar. Cuenta el "problema real" que sufre un paquete de TeMU al llegar a esa región y anticipa la mecánica que lo hace jugable (Diseno_Niveles_TeMU_Express_v2.txt). Los niveles, regiones y mecánicas siguen la tabla de Decisiones_MVP_TeMU_ExpressFinal.txt (puntos 1 y 5).

Además del mensaje de inicio, se proponen un mensaje de victoria y uno de derrota por región. La derrota aparece cuando se rompe una caja frágil o cae una caja llave en un agujero (Decisiones, punto 2).

---

## 1-1 China (HUD: CHINA)

**Mecánicas:** cajas normales, caja llave, cerrojo y muros.

**Mensaje de inicio**
> La aduana bloqueó tu compra y pide tu documento para todo.
> Llevá la CAJA LLAVE hasta el CERROJO para abrir los muros.
> Si la sacás del cerrojo, te retienen el documento otra vez.

**Victoria:** "Documento aprobado. El paquete sigue viaje."
**Derrota:** "Trámite rechazado. Probá con UNDO o REINICIAR."

---

## 2-1 Estados Unidos (HUD: EEUU)

**Mecánicas:** caja frágil, caja llave, cerrojo y muros.

**Mensaje de inicio**
> Control sorpresa: los inspectores federales revisan todo de más.
> La CAJA FRÁGIL aguanta pocos empujes; mirá sus grietas.
> Al cuarto empujón se rompe y perdés el envío.

**Victoria:** "Inspección superada. Nada roto, nada confiscado."
**Derrota:** "Tu paquete no sobrevivió a la inspección."

---

## 3-1 Alemania (HUD: ALEMANIA)

**Mecánicas:** agujeros y caja frágil.

**Mensaje de inicio**
> Los impuestos de importación cuestan más que el producto.
> Empujá una caja a cada AGUJERO para taparlo: es lo que pagás de aranceles.
> Trajiste una caja de más por cada agujero. Elegí bien cuál sacrificás.

**Victoria:** "Impuestos pagados. Lo que quedó llegó a destino."
**Derrota:** "Se perdió la caja equivocada. El envío no se puede completar."

---

## 4-1 Argentina - Chile (HUD: ARG-CHILE)

**Mecánicas:** terreno resbaladizo (hielo), cerrojo y muros, agujero y recargo de Undo.

**Mensaje de inicio**
> Cupos de compra y papeleo: cualquier error te sale carísimo.
> En el HIELO la caja patina hasta chocar o salir del hielo.
> Acá cada UNDO cuesta el doble en el puntaje (x2).

**Victoria:** "Trámite aprobado al primer intento. Milagro."
**Derrota:** "Error en el formulario. Volvé a empezar el trámite."

---

## 4-2 Ecuador - Colombia (HUD: ECU-COL)

**Mecánicas:** cinta, caja frágil, cerrojo y muros.

**Mensaje de inicio**
> La aduana retiene tu paquete y lo pasa por el escáner.
> La CINTA arrastra la caja hacia donde marca la flecha.
> Vos no podés pisarla: solo mirás cómo se lo llevan.

**Victoria:** "Escaneo terminado. Paquete liberado."
**Derrota:** "El paquete quedó retenido en la inspección."

---

## 4-3 Perú - Venezuela (HUD: PERU-VEN) - nivel final

**Mecánicas:** portales, cinta, hielo, agujero, caja frágil, caja llave, cerrojo y muros.

**Mensaje de inicio**
> El correo no llega, así que el paquete viaja por casilleros privados.
> Cada PORTAL lleva la caja al otro portal del mismo color.
> Es la última aduana: todo lo que aprendiste junto.

**Victoria:** "¡Entrega completada! El paquete esquivó todas las aduanas."
**Derrota:** "El casillero falló. Intentá otra ruta."

**Mensaje del final del juego (después de 4-3)**
> TeMU Express cumplió: el paquete llegó de China a Sudamérica.
> Puntaje total (1UP): [puntaje]
> Gracias por elegir TeMU Express. Ojalá la próxima no te cobre la aduana.

---

## Personajes

Base: el repartidor de TeMU que diseñó el grupo (archivo del compañero), pasado a pixel art en `assets/img/personaje/` en 4 direcciones. En el diagrama 4 cada fábrica regional crea su `AspectoRepartidor`, así que hay un repartidor por familia, cada uno con su color.

| Familia | Nombre | De dónde sale | Sprite |
|---|---|---|---|
| China | **Xi Dada** ("Tío Xi" o "Gran Papá Xi") | Apodo del presidente de China. | `repartidor_azul` |
| EEUU | **Donny Trump** | Apodo de Donald Trump. | `repartidor_amarillo` |
| Alemania | **Der Führer** | Seudónimo o título de Adolfo Hitler. | `repartidor_rojo` |
| Sudamérica | **Simoncito** o **El negro de América** | Apodos de Simón Bolívar. | `repartidor_verde` |

**Esqueleto:** se propone mostrarlo en la pantalla de derrota, cuando se rompe una caja frágil o cae una caja llave en un agujero (`repartidor_verde/esqueleto.png`).

**A confirmar por el grupo:**
- Si el esqueleto va en la derrota.
