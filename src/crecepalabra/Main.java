package crecepalabra;

import java.io.*;

/**
 *
 * @author Ramses-Kevin
 */
public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader num = new BufferedReader(new InputStreamReader(System.in));
        // Declaraciones.
        String line;
        String opcionDc;
        int opcion = 0;
        int numFichas = 0;
        char opcionAdd = ' ';
        final char[] letrasAleatorias;
        // Presentación.
        Fichas.Presentacion();
        System.out.print("Escoge opción: ");
        line = num.readLine();

        while (!(line.equals("0") || line.equals("1") || line.equals("2"))) {
            Fichas.Presentacion();
            System.out.print("Escoge opción: ");
            line = num.readLine();
        }

        if (line.equals("1")) {
            numFichas = 15;
        }
        if (line.equals("2")) {
            System.out.print("Introduce un número de fichas de 5 a 25: ");
            line = num.readLine();
            numFichas = Integer.parseInt(line);
            while (!(numFichas >= 5 && numFichas <= 25)) {
                System.out.print("Introduce un número de fichas de 5 a 25: ");
                line = num.readLine();
                numFichas = Integer.parseInt(line);
            }
        }
        if (!(line.equals("0"))) {
            // Obtenemos letras Aleatorias.
            letrasAleatorias = Fichas.obtenerLetrasAleatorias(numFichas);
            // Presentación letras aleatorias.
            System.out.println("Las letras aleatorias son: ");
            Fichas.presentacionLetrasAleatorias(letrasAleatorias);
            // Inicialización de objetos para comenzar la partida.
            Fichas nuevaPartida = new Fichas();
            Palabra palabraUsuario = new Palabra();
            Palabra pAnterior = new Palabra();
            //
            System.out.println("3. Utilizar comodín (solo tienes 1)");
            System.out.print("Introduce una palabra (dejar en blanco para rendirse): ");
            palabraUsuario = Palabra.leer();
            // Bucle para controlar la rendición del usuario.
            while (!palabraUsuario.rendicion() && nuevaPartida.numPalabras() < 100) {
                if (palabraUsuario.toString().equals("3")) {
                    if (nuevaPartida.comodin()) {
                        System.out.println(' ');
                        Fichas.comodin(pAnterior, letrasAleatorias, nuevaPartida);
                    } else {
                        System.out.println(' ');
                        System.out.println("No te quedan comodines para utilizar.");
                    }
                    palabraUsuario = pAnterior;
                } else if (((palabraUsuario.es_igual(pAnterior) || Fichas.es_repetida(palabraUsuario))) && (nuevaPartida.numPalabras() > 0)) {
                    System.out.println(' ');
                    System.out.println("No se puede repetir la palabra.");
                    palabraUsuario = pAnterior;
                } else if ((palabraUsuario.LongPalabra() < pAnterior.LongPalabra()) && (nuevaPartida.numPalabras() > 0)) {
                    System.out.println(' ');
                    System.out.println("La nueva palabra no puede ser más corta que la anterior: '" + pAnterior + "'.");
                    palabraUsuario = pAnterior;
                } else if ((Fichas.longDiferencia(palabraUsuario, pAnterior) > 1) && (nuevaPartida.numPalabras() > 0)) {
                    System.out.println(' ');
                    System.out.println("La palabra no puede crecer " + Fichas.longDiferencia(palabraUsuario, pAnterior) + " fichas: '" + pAnterior + "'.");
                    palabraUsuario = pAnterior;
                } else if (Fichas.palabraCorrecta(palabraUsuario, letrasAleatorias) && palabraUsuario.longInicial()) {
                    if (nuevaPartida.numPalabras() == 0 && !(Fichas.comprobarDiccionario(palabraUsuario))) {
                        do {
                            Fichas.palabraPregunta();
                            opcionDc = num.readLine();
                        } while (!(opcionDc.equals("y")) && !(opcionDc.equals("n")));
                        if (opcionDc.equals("y")) {
                            Fichas.incluirPalabra(palabraUsuario);
                            nuevaPartida.palabraCorrecta(palabraUsuario);
                            nuevaPartida.sumarPalabra();
                        }
                    }
                    if (palabraUsuario.LongPalabra() == pAnterior.LongPalabra()) {
                        if (Fichas.susDoble(palabraUsuario, pAnterior)) {
                            System.out.println(' ');
                            System.out.println("No se puede cambiar más de dos fichas entre palabras.");
                            palabraUsuario = pAnterior;
                        } else if (Fichas.cOrden(palabraUsuario, pAnterior)) {
                            if (Fichas.comprobarDiccionario(palabraUsuario)) {
                                System.out.println(' ');
                                nuevaPartida.sumOrden();
                                Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                Fichas.letra3(nuevaPartida, palabraUsuario);
                                nuevaPartida.palabraCorrecta(palabraUsuario);
                                nuevaPartida.sumarPalabra();
                            } else {
                                do {
                                    Fichas.palabraPregunta();
                                    opcionDc = num.readLine();
                                } while (!(opcionDc.equals("y")) && !(opcionDc.equals("n")));
                                if (opcionDc.equals("y")) {
                                    Fichas.incluirPalabra(palabraUsuario);
                                    System.out.println(' ');
                                    nuevaPartida.sumOrden();
                                    Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                    Fichas.letra3(nuevaPartida, palabraUsuario);
                                    nuevaPartida.palabraCorrecta(palabraUsuario);
                                    nuevaPartida.sumarPalabra();
                                }
                            }
                        } else {
                            if (Fichas.sustitucion(palabraUsuario, pAnterior)) {
                                if (Fichas.ordSus(palabraUsuario, pAnterior)) {
                                    if (Fichas.comprobarDiccionario(palabraUsuario)) {
                                        System.out.println(' ');
                                        nuevaPartida.sumOrdenSus();
                                        Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                        Fichas.letra3(nuevaPartida, palabraUsuario);
                                        nuevaPartida.palabraCorrecta(palabraUsuario);
                                        nuevaPartida.sumarPalabra();
                                    } else {
                                        do {
                                            Fichas.palabraPregunta();
                                            opcionDc = num.readLine();
                                        } while (!(opcionDc.equals("y")) && !(opcionDc.equals("n")));
                                        if (opcionDc.equals("y")) {
                                            Fichas.incluirPalabra(palabraUsuario);
                                            System.out.println(' ');
                                            nuevaPartida.sumOrdenSus();
                                            Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                            Fichas.letra3(nuevaPartida, palabraUsuario);
                                            nuevaPartida.palabraCorrecta(palabraUsuario);
                                            nuevaPartida.sumarPalabra();
                                        }
                                    }
                                } else {
                                    if (Fichas.comprobarDiccionario(palabraUsuario)) {
                                        System.out.println(' ');
                                        nuevaPartida.sumSus();
                                        Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                        Fichas.letra3(nuevaPartida, palabraUsuario);
                                        nuevaPartida.palabraCorrecta(palabraUsuario);
                                        nuevaPartida.sumarPalabra();
                                    } else {
                                        do {
                                            Fichas.palabraPregunta();
                                            opcionDc = num.readLine();
                                        } while (!(opcionDc.equals("y")) && !(opcionDc.equals("n")));
                                        if (opcionDc.equals("y")) {
                                            Fichas.incluirPalabra(palabraUsuario);
                                            System.out.println(' ');
                                            nuevaPartida.sumSus();
                                            Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                            Fichas.letra3(nuevaPartida, palabraUsuario);
                                            nuevaPartida.palabraCorrecta(palabraUsuario);
                                            nuevaPartida.sumarPalabra();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (palabraUsuario.LongPalabra() == pAnterior.LongPalabra() + 1) {
                        if (Fichas.mLetras(palabraUsuario, pAnterior)) {
                            if (Fichas.agLetra(palabraUsuario, pAnterior)) {
                                if (Fichas.comprobarDiccionario(palabraUsuario)) {
                                    System.out.println(' ');
                                    nuevaPartida.sumAgLetra();
                                    Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                    Fichas.letra3(nuevaPartida, palabraUsuario);
                                    nuevaPartida.palabraCorrecta(palabraUsuario);
                                    nuevaPartida.sumarPalabra();
                                } else {
                                    do {
                                        Fichas.palabraPregunta();
                                        opcionDc = num.readLine();
                                    } while (!(opcionDc.equals("y")) && !(opcionDc.equals("n")));
                                    if (opcionDc.equals("y")) {
                                        Fichas.incluirPalabra(palabraUsuario);
                                        System.out.println(' ');
                                        nuevaPartida.sumAgLetra();
                                        Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                        Fichas.letra3(nuevaPartida, palabraUsuario);
                                        nuevaPartida.palabraCorrecta(palabraUsuario);
                                        nuevaPartida.sumarPalabra();
                                    }
                                }
                            } else {
                                if (Fichas.comprobarDiccionario(palabraUsuario)) {
                                    System.out.println(' ');
                                    nuevaPartida.sumAgLetraOrd();
                                    Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                    Fichas.letra3(nuevaPartida, palabraUsuario);
                                    nuevaPartida.palabraCorrecta(palabraUsuario);
                                    nuevaPartida.sumarPalabra();
                                } else {
                                    do {
                                        Fichas.palabraPregunta();
                                        opcionDc = num.readLine();
                                    } while (!(opcionDc.equals("y")) && !(opcionDc.equals("n")));
                                    if (opcionDc.equals("y")) {
                                        Fichas.incluirPalabra(palabraUsuario);
                                        System.out.println(' ');
                                        nuevaPartida.sumAgLetraOrd();
                                        Fichas.Palabra7(nuevaPartida, palabraUsuario);
                                        Fichas.letra3(nuevaPartida, palabraUsuario);
                                        nuevaPartida.palabraCorrecta(palabraUsuario);
                                        nuevaPartida.sumarPalabra();
                                    }
                                }
                            }
                        } else {
                            System.out.println(' ');
                            System.out.println("No se puede cambiar fichas y añadir en la misma jugada.");
                            palabraUsuario = pAnterior;
                        }
                    }
                    if (nuevaPartida.numPalabras() == 0) {
                        nuevaPartida.palabraCorrecta(palabraUsuario);
                        nuevaPartida.sumarPalabra();
                    }
                    System.out.println("Puntuación actual: " + nuevaPartida.obtPuntuacion());
                    System.out.println(' ');
                } else {
                    System.out.println(' ');
                    System.out.println("No se puede formar la palabra '" + palabraUsuario + "'");
                    palabraUsuario = pAnterior;
                }
                pAnterior = palabraUsuario;
                System.out.println(' ');
                Fichas.presentacionLetrasAleatorias(letrasAleatorias);
                System.out.println("Palabra Anterior: " + pAnterior);
                if (nuevaPartida.comodin()) {
                    System.out.println("3. Utilizar comodín (1 comodín)");
                } else {
                    System.out.println("3. Utilizar comodín (No te quedan)");
                }
                System.out.print("Introduce una palabra (dejar en blanco para rendirse): ");
                palabraUsuario = Palabra.leer();
            }
            // ==========================
            // Resultado Final.
            nuevaPartida.resultado();
            nuevaPartida.resPalabraCorrecta();
        }
    }
}
