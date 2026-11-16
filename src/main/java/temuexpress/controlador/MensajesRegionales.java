package temuexpress.controlador;

import java.util.List;
import java.util.Map;

/**
 * Textos de cada nivel: páginas del mensaje de inicio (historia del repartidor y la misión), frases
 * del repartidor al perder, al entregar una caja y cuando una caja cae a un agujero. Base:
 * docs/textos/mensajes_regionales.md.
 */
final class MensajesRegionales {

    /** Una página de texto: el mensaje de inicio o el cierre del juego. */
    record Pagina(String titulo, String[] lineas) {
        Pagina {
            lineas = lineas.clone();
        }

        @Override
        public String[] lineas() {
            return lineas.clone();
        }
    }

    private record Mensajes(
            List<Pagina> paginas,
            String derrota,
            List<String> frasesDerrota,
            List<String> entregas,
            List<String> caidas) {}

    private static final Map<String, Mensajes> POR_NIVEL =
            Map.of(
                    "1-1",
                    new Mensajes(
                            List.of(
                                    new Pagina(
                                            "XI DADA",
                                            new String[] {
                                                "Xi Dada es el repartidor más antiguo de TeMU"
                                                        + " Express.",
                                                "Empezó en un depósito de Shenzhen armando cajas de"
                                                        + " a millones, sin un solo día libre.",
                                                "Dicen que nunca perdió un paquete... porque nunca"
                                                        + " lo dejaron salir del depósito.",
                                                "Hoy, por fin, le toca su primer envío al exterior."
                                            }),
                                    new Pagina(
                                            "CHINA",
                                            new String[] {
                                                "La aduana bloqueó tu compra y pide tu documento"
                                                        + " para todo.",
                                                "Llevá la CAJA LLAVE hasta el CERROJO para abrir"
                                                        + " los muros.",
                                                "Si la sacás del cerrojo, te retienen el documento"
                                                        + " otra vez."
                                            })),
                            "Trámite rechazado.",
                            List.of(
                                    "Paquete confiscado. Ahora son 40 formularios más.",
                                    "Ni en Shenzhen me pasaba esto. Volvamos a intentar.",
                                    "La aduana gana esta vez. La próxima, no."),
                            List.of(
                                    "¡Hǎo! (¡Bien!) Paquete entregado.",
                                    "Xièxie, cliente. Vuelva pronto.",
                                    "Otro envío de Shenzhen. Quedan mil millones."),
                            List.of()),
                    "2-1",
                    new Mensajes(
                            List.of(
                                    new Pagina(
                                            "DONALD \"DONNY\" TRUMP",
                                            new String[] {
                                                "Donny empezó de abajo: con un pequeño préstamo de"
                                                        + " su papá.",
                                                "El préstamo era una empresa de paquetería entera.",
                                                "Un día preguntó: \"¿Y los paquetes cómo llegan?\"."
                                                        + " Silencio total.",
                                                "Hoy reparte él mismo. Primer trabajo de su vida."
                                                        + " Tremendo."
                                            }),
                                    new Pagina(
                                            "EEUU",
                                            new String[] {
                                                "Control sorpresa: los inspectores federales revisan"
                                                        + " todo de más.",
                                                "La CAJA FRÁGIL aguanta pocos empujes; mirá sus"
                                                        + " grietas.",
                                                "Al cuarto empujón se rompe y perdés el envío."
                                            })),
                            "Tu paquete no sobrevivió a la inspección.",
                            List.of(
                                    "¿Rota? Fake news. Esa caja estaba perfecta.",
                                    "Uy, se rompió esta caja. Mejor le mando un cheque de 10 mil"
                                            + " dólares.",
                                    "Uyyy, se rompió esta caja. Papá, llama a los abogados, ¡pero"
                                            + " rápido!"),
                            List.of(
                                    "Entregado. Nadie entrega como yo, todos lo dicen.",
                                    "Una entrega enorme. Histórica. La mejor.",
                                    "Pónganle mi nombre a esa caja. En dorado."),
                            List.of()),
                    "3-1",
                    new Mensajes(
                            List.of(
                                    new Pagina(
                                            "DER FÜHRER",
                                            new String[] {
                                                "Antes de ser el malo de esta historia, era un"
                                                        + " joven pintor que vivía en Viena.",
                                                "Pintaba paisajes y edificios todo el día; le"
                                                        + " gustaba pintar demasiado.",
                                                "Reparte paquetes de TeMU para juntar plata y"
                                                        + " entrar a la gran Academia de Bellas"
                                                        + " Artes.",
                                                "Cada caja que entrega es una clase de pintura"
                                                        + " más. No le falles."
                                            }),
                                    new Pagina(
                                            "ALEMANIA",
                                            new String[] {
                                                "Los impuestos de importación cuestan más que el"
                                                        + " producto.",
                                                "Empujá una caja a cada AGUJERO para taparlo: son"
                                                        + " los aranceles.",
                                                "Trajiste una caja de más por cada agujero. Elegí"
                                                        + " bien cuál sacrificás."
                                            })),
                            "Se perdió la caja equivocada.",
                            List.of(
                                    "Nein, nein, nein. Me arruinaste el cuadro.",
                                    "Rechazado otra vez... como en la escuela de arte.",
                                    "Sin esta entrega no llego a pagar la Academia."),
                            List.of(
                                    "Sehr gut! Una obra maestra.",
                                    "Wunderbar, esto merece un marco.",
                                    "Danke! Una clase de pintura más."),
                            List.of(
                                    "Ach du lieber! ¡Ay, Dios mío! Al pozo el paquete se fue, ja.",
                                    "Tschüss, Paket! ¡Chau, paquete! Muy bien caído, ja.",
                                    "Ordnung muss sein! Orden ante todo: un agujero, una caja,"
                                            + " ja.",
                                    "Wunderbar! ¡Maravilloso! El arancel pagado está, ja.")),
                    "4-1",
                    new Mensajes(
                            List.of(
                                    new Pagina(
                                            "SIMONCITO",
                                            new String[] {
                                                "Simoncito llegó a Sudamérica con un sueño: unir"
                                                        + " todo el continente con envíos.",
                                                "Le dicen así desde chico, por su abuelo, que"
                                                        + " cruzó los Andes a caballo con cartas"
                                                        + " bastante subidas de tono para su"
                                                        + " abuela.",
                                                "Ahora él los cruza con paquetes de TeMU y un termo"
                                                        + " de mate bajo el brazo."
                                            }),
                                    new Pagina(
                                            "ARG-CHILE",
                                            new String[] {
                                                "Cupos de compra y papeleo: cualquier error te sale"
                                                        + " carísimo.",
                                                "En el HIELO, la caja patina hasta chocar o salir"
                                                        + " del hielo.",
                                                "Acá, cada UNDO cuesta el doble en el puntaje (x2)."
                                            })),
                            "Error en el formulario.",
                            List.of(
                                    "Uh, se congeló el trámite. Hay que arrancar de nuevo.",
                                    "Ni el mate me salva de esta cordillera de papeles.",
                                    "Se patinó todo, che. Volvamos a probar."),
                            List.of(
                                    "¡Uno menos, che!",
                                    "¡Bacán, llegó al toque!",
                                    "¡Joya, entregado antes del cepo!"),
                            List.of(
                                    "¡Se fue al pozo, che! Ni lo vi.",
                                    "¡Chuta, se lo tragó la tierra!",
                                    "¿Cachai? Un hoyo menos.",
                                    "¡Bajó más rápido que el peso!")),
                    "4-2",
                    new Mensajes(
                            List.of(
                                    new Pagina(
                                            "SIMONCITO",
                                            new String[] {
                                                "Simoncito sigue viaje hacia el norte, entre"
                                                        + " Ecuador y Colombia.",
                                                "En cada pueblo le invitan un café y le preguntan"
                                                        + " por qué tarda tanto el correo.",
                                                "Su lema: ningún paquete queda atrás, aunque se lo"
                                                        + " lleve la cinta."
                                            }),
                                    new Pagina(
                                            "ECU-COL",
                                            new String[] {
                                                "La aduana retiene tu paquete y lo pasa por el"
                                                        + " escáner.",
                                                "La CINTA arrastra la caja hacia donde marca la"
                                                        + " flecha.",
                                                "Vos no podés pisarla: solo mirás cómo se lo"
                                                        + " llevan."
                                            })),
                            "El paquete quedó retenido en la inspección.",
                            List.of(
                                    "Se lo llevó la cinta. Ni el escáner lo encuentra.",
                                    "Retenido otra vez. Me voy a tomar otro café.",
                                    "Ese paquete no pasó el control. Probemos de nuevo."),
                            List.of(
                                    "¡Qué chévere, parcero!",
                                    "¡Listo, mijo, paquete liberado!",
                                    "¡Bacano, uno menos pa'l escáner!"),
                            List.of()),
                    "4-3",
                    new Mensajes(
                            List.of(
                                    new Pagina(
                                            "SIMONCITO",
                                            new String[] {
                                                "Llegó la última aduana. Simoncito recorrió medio"
                                                        + " mundo por este paquete.",
                                                "Xi Dada lo despachó, Donny lo inspeccionó y el"
                                                        + " pintor de Alemania lo cruzó por"
                                                        + " Europa.",
                                                "Si lo entrega, TeMU Express lo nombra Repartidor"
                                                        + " del Año."
                                            }),
                                    new Pagina(
                                            "PERU-VEN",
                                            new String[] {
                                                "El correo no llega: el paquete viaja por"
                                                        + " casilleros privados.",
                                                "Cada PORTAL lleva la caja al otro portal del mismo"
                                                        + " color.",
                                                "Es la última aduana: todo lo que aprendiste junto."
                                            })),
                            "El casillero falló.",
                            List.of(
                                    "El casillero se tragó el paquete. Otra ruta, causa.",
                                    "Tan cerca del final... Vamos de nuevo.",
                                    "Ni con portales llega este correo. Otra vez."),
                            List.of(
                                    "¡Llegó, causa! Está bravazo.",
                                    "¡Chévere, pana!",
                                    "¡Épale, otro casillero cumplido!"),
                            List.of(
                                    "¡Asu mare, se lo tragó el hueco!",
                                    "¡Qué chimbo, pana, se fue pa'l hueco!",
                                    "¡Chamo, ese casillero no tenía fondo!",
                                    "¡Habla, causa! Un hueco menos en la ruta.")));

    private MensajesRegionales() {}

    private static Mensajes de(String nivel) {
        Mensajes m = POR_NIVEL.get(nivel);
        if (m == null) throw new IllegalArgumentException("Nivel sin mensajes: " + nivel);
        return m;
    }

    static List<Pagina> paginas(String nivel) {
        return de(nivel).paginas();
    }

    /** Frase del repartidor (va rotando) y lo que pasó. */
    static String[] derrota(String nivel, int vez) {
        Mensajes m = de(nivel);
        return new String[] {"\"" + rotar(m.frasesDerrota(), vez) + "\"", m.derrota()};
    }

    /** Lo que dice el repartidor al entregar una caja (va rotando). */
    static String entrega(String nivel, int vez) {
        return rotar(de(nivel).entregas(), vez);
    }

    /** Lo que dice cuando una caja cae a un agujero; null si el nivel no tiene frases de caída. */
    static String caida(String nivel, int vez) {
        List<String> frases = de(nivel).caidas();
        return frases.isEmpty() ? null : rotar(frases, vez);
    }

    private static String rotar(List<String> frases, int vez) {
        return frases.get(Math.floorMod(vez, frases.size()));
    }

    /** Cierre del juego: qué fue de cada repartidor. */
    static Pagina cierreRepartidores() {
        return new Pagina(
                "LOS REPARTIDORES",
                new String[] {
                    "Xi Dada se tomó su primer día libre en 40 años. Lo pasó armando cajas.",
                    "Donny compró a la competencia. Dice que repartir es fácil, el más fácil.",
                    "El pintor juntó la plata para la Academia. El examen... mejor ni hablar.",
                    "Simoncito es Repartidor del Año. La abuela todavía guarda las cartas."
                });
    }

    static String[] finalDelJuego(int puntajeTotal) {
        return new String[] {
            "TeMU Express llevó el paquete de China a Sudamérica.",
            "Puntaje total (1UP): " + puntajeTotal,
            "Gracias por elegir TeMU Express."
        };
    }
}
