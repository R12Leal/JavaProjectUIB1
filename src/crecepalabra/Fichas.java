package crecepalabra;

import java.io.*;
import java.util.*;

/**
 * @author Ramses-Kevin
 */
public class Fichas {

    // Atributos.
    private static int palabras;
    private int puntuacion;
    private boolean comodin;
    private static final char[] abecedario = "abcdefghijklmnñopqrstuvwxyz".toCharArray();
    private static Palabra[] correctas;
    private static TablaFrecuencias tf_nueva;
    private static TablaFrecuencias tf_ant;

    // Constructor.
    public Fichas() {
        palabras = 0;
        puntuacion = 0;
        comodin = true;
        correctas = new Palabra[100];
    }

    // Métodos de clase.
    private static void iniFrecAbc() {
        tf_nueva = new TablaFrecuencias();
        tf_ant = new TablaFrecuencias();
        for (int i = 0; i < abecedario.length; i++) {
            tf_nueva.actualizarFrecuencia(abecedario[i]);
            tf_ant.actualizarFrecuencia(abecedario[i]);
        }
    }

    public static void Presentacion() {
        System.out.println("JUEGO CRECE-PALABRA - CASTELLANO");
        System.out.println("Prática programación I | 2013-2014");
        System.out.println("Selecciona una opción:");
        System.out.println(' ');
        System.out.println("1. Jugar partida con 15 fichas");
        System.out.println("2. Jugar partida indicando el número de fichas");
        System.out.println("0. Exit");
        System.out.println(' ');
    }

    public static void presentacionLetrasAleatorias(char[] letrasAleatorias) {
        for (int i = 0; i < letrasAleatorias.length; i++) {
            System.out.print("|'" + letrasAleatorias[i] + "'");
        }
        System.out.println("|");
    }

    private static int obtenerNumAleatorio(int numRango) {
        Random num = new Random();
        return num.nextInt(numRango);
    }

    public static char[] obtenerLetrasAleatorias(int numLetras) {
        char[] letrasAleatorias = new char[numLetras];
        int totalAbecedario = abecedario.length;
        //
        for (int contadorLetras = 0; contadorLetras < numLetras; contadorLetras++) {
            letrasAleatorias[contadorLetras] = abecedario[obtenerNumAleatorio(totalAbecedario)];
        }
        return letrasAleatorias;
    }

    public static boolean comprobarDiccionario(Palabra palabra) throws FileNotFoundException, IOException {
        int maximo = 100;
        char[] palabraDiccionario = new char[maximo];
        char[] palabraUsuario = palabra.toString().toCharArray();
        char caracter = ' ';
        int cont1 = 0;
        int cont2 = 0;
        int valorCaracter = 0;
        boolean encontrado = false;
        //
        FileReader diccionario = new FileReader("diccionario.txt");
        valorCaracter = diccionario.read();
        while (valorCaracter != -1 && !encontrado) {
            caracter = (char) valorCaracter;
            while (caracter != '\n') {
                palabraDiccionario[cont1] = caracter;
                cont1++;
                valorCaracter = diccionario.read();
                caracter = (char) valorCaracter;
            }
            for (cont2 = 0; (palabraDiccionario[cont2] == palabraUsuario[cont2]) && cont2 < palabraUsuario.length - 1; cont2++) {
                encontrado = true;
            }
            if (!(palabraDiccionario[cont2] == palabraUsuario[cont2])) {
                encontrado = false;
            }
            palabraDiccionario = new char[maximo];
            cont1 = 0;
            cont2 = 0;
            valorCaracter = diccionario.read();
        }
        diccionario.close();
        return encontrado;
    }

    public static void incluirPalabra(Palabra palabra) throws IOException {
        FileWriter fichero = new FileWriter("diccionario.txt", true);
        fichero.write(palabra.toString());
        fichero.write('\n');
        fichero.close();
    }

    public static boolean palabraCorrecta(Palabra palabra, char[] letras) {
        int contador = 0;
        int contadorLetras = 0;
        int longitudPalabra = palabra.LongPalabra();
        int longitudLetras = letras.length;
        boolean letraEncontrada = true;
        while (contador < longitudPalabra && letraEncontrada) {
            while (contadorLetras < longitudLetras && palabra.letra(contador) != letras[contadorLetras]) {
                contadorLetras++;
            }
            if (contadorLetras == longitudLetras) {
                contadorLetras--;
            }
            if (palabra.letra(contador) == letras[contadorLetras]) {
                letraEncontrada = true;
            } else {
                letraEncontrada = false;
            }
            contador++;
            contadorLetras = 0;
        }
        return letraEncontrada && compTf(palabra, letras);
    }

    private static boolean compTf(Palabra pNueva, char[] letrasAleatorias) {
        iniFrecAbc();
        //
        for (int i = 0; i < letrasAleatorias.length; i++) {
            tf_nueva.actualizarFrecuencia(letrasAleatorias[i]);
        }
        for (int i = 0; i < pNueva.LongPalabra(); i++) {
            tf_ant.actualizarFrecuencia(pNueva.letra(i));
        }
        int contador = 0;
        int i = 0;
        while (i < abecedario.length) {
            if (tf_ant.letraTf(i) > tf_nueva.letraTf(i)) {
                contador++;
            }
            i++;
        }
        return contador == 0;
    }

    public static boolean sustitucion(Palabra pNueva, Palabra pAnterior) {
        iniFrecAbc();
        for (int i = 0; i < pNueva.LongPalabra(); i++) {
            tf_nueva.actualizarFrecuencia(pNueva.letra(i));
        }
        for (int i = 0; i < pAnterior.LongPalabra(); i++) {
            tf_ant.actualizarFrecuencia(pAnterior.letra(i));
        }
        //
        int valor = 0;
        int i = 0;
        while (i < abecedario.length && tf_nueva.letraTf(i) == tf_ant.letraTf(i)) {
            i++;
        }
        if (tf_nueva.letraTf(i) != tf_ant.letraTf(i)) {
            valor++;
        }
        // Return
        return valor == 1;
    }

    public static boolean susDoble(Palabra pNueva, Palabra pAnterior) {
        int i = 0;
        int x = 0;
        int contDoble = 0;
        while (i < pNueva.LongPalabra()) {
            while (pNueva.letra(i) != pAnterior.letra(x) && x < pAnterior.LongPalabra()) {
                x++;
            }
            if (pNueva.letra(i) != pAnterior.letra(x)) {
                contDoble++;
            }
            i++;
            x = 0;
        }
        return contDoble >= 2;
    }

    public static boolean cOrden(Palabra pNueva, Palabra pAnterior) {
        iniFrecAbc();
        for (int i = 0; i < pNueva.LongPalabra(); i++) {
            tf_nueva.actualizarFrecuencia(pNueva.letra(i));
        }
        for (int i = 0; i < pAnterior.LongPalabra(); i++) {
            tf_ant.actualizarFrecuencia(pAnterior.letra(i));
        }
        int valor = 0;
        int i = 0;
        while (i < abecedario.length && tf_nueva.letraTf(i) == tf_ant.letraTf(i)) {
            i++;
        }
        return i == abecedario.length;
    }

    public static boolean ordSus(Palabra pNueva, Palabra pAnterior) {
        boolean sustitucion = sustitucion(pNueva, pAnterior);
        int contDif = 0;
        for (int i = 0; i < pNueva.LongPalabra(); i++) {
            if (pNueva.letra(i) != pAnterior.letra(i)) {
                contDif++;
            }
        }
        return sustitucion && (contDif >= 2);
    }

    public static boolean agLetra(Palabra pNueva, Palabra pAnterior) {
        int i = 0;
        int x = 0;

        while (i < pAnterior.LongPalabra() && x < pNueva.LongPalabra()) {
            if (pNueva.letra(x) == pAnterior.letra(i)) {
                i++;
                x++;
            } else {
                x++;
            }
        }
        if (x == i) {
            return true;
        }

        return (x == i + 1);
    }

    public static boolean mLetras(Palabra pNueva, Palabra pAnterior) {
        iniFrecAbc();
        for (int i = 0; i < pNueva.LongPalabra(); i++) {
            tf_nueva.actualizarFrecuencia(pNueva.letra(i));
        }
        for (int i = 0; i < pAnterior.LongPalabra(); i++) {
            tf_ant.actualizarFrecuencia(pAnterior.letra(i));
        }
        int valor = 0;
        int i = 0;
        while (i < abecedario.length) {
            if (tf_nueva.letraTf(i) != tf_ant.letraTf(i)) {
                valor++;
            }
            i++;
        }
        return valor == 1;
    }

    // Puntuaciones Adicionales.
    private static boolean longitud7(Palabra pNueva) {
        return pNueva.LongPalabra() >= 7;
    }

    private static boolean tresLetras(Palabra pNueva) {
        TablaFrecuencias tf_palabra = new TablaFrecuencias();
        for (int i = 0; i < abecedario.length; i++) {
            tf_palabra.actualizarFrecuencia(abecedario[i]);
        }
        for (int i = 0; i < pNueva.LongPalabra(); i++) {
            tf_palabra.actualizarFrecuencia(pNueva.letra(i));
        }
        //
        int i = 0;
        int valor = 0;
        while (i < abecedario.length) {
            if (tf_palabra.letraTf(i) > 3) {
                valor++;
            }
            i++;
        }
        return valor > 0;
    }

    public static void Palabra7(Fichas partida, Palabra pNueva) {
        if ((longitud7(pNueva))) {
            partida.long7(pNueva.LongPalabra());
        }
    }

    public static void letra3(Fichas partida, Palabra pNueva) {
        if ((partida.tresLetras(pNueva))) {
            partida.mismaLetra();
        }
    }

    // Fin puntuaciones adicionales.
    public static int longDiferencia(Palabra pNueva, Palabra pAnterior) {
        return pNueva.LongPalabra() - pAnterior.LongPalabra();
    }

    public static boolean es_repetida(Palabra pNueva) {
        int i = 0;
        boolean encontrado = false;
        Palabra correcta = correctas[i];
        while (i < palabras && !(encontrado)) {
            encontrado = pNueva.toString().equals(correcta.toString());
            i++;
            correcta = correctas[i];
        }
        return encontrado;
    }

    public static void palabraPregunta() {
        System.out.println(' ');
        System.out.println("No existe esta palabra en el diccionario.");
        System.out.print("¿Desea añadir la palabra al diccionario? (y/n): ");
    }

    public static void comodin(Palabra pAnterior, char[] letrasAleatorias, Fichas partida) throws FileNotFoundException, IOException {
        int contCaracter = 0;
        int longPalabra = 0;
        int contImp = 0;
        int contFallo = 0;
        boolean pAnt = false;
        boolean frecLetra = true;
        boolean enCorrectas = false;
        String pDiccionario = "";
        Palabra pCorrecta;
        TablaFrecuencias letras = new TablaFrecuencias();
        FileReader diccionario = new FileReader("diccionario.txt");
        iniFrecAbc();
        for (int i = 0; i < abecedario.length; i++) {
            letras.actualizarFrecuencia(abecedario[i]);
        }
        for (int i = 0; i < letrasAleatorias.length; i++) {
            letras.actualizarFrecuencia(letrasAleatorias[i]);
        }
        if (partida.numPalabras() == 0) {

            int valor = diccionario.read();
            char caracter = (char) valor;
            while (valor != -1) {
                while (caracter != '\n') {
                    while (contCaracter < letrasAleatorias.length && caracter != letrasAleatorias[contCaracter]) {
                        contCaracter++;
                    }
                    if (contCaracter != letrasAleatorias.length) {
                        pDiccionario = pDiccionario + String.valueOf(caracter);
                        tf_nueva.actualizarFrecuencia(caracter);
                        longPalabra++;
                    } else {
                        pDiccionario = "";
                        longPalabra = 0;
                        while (caracter != '\n') {
                            valor = diccionario.read();
                            caracter = (char) valor;
                        }
                    }
                    if (caracter != '\n') {
                        valor = diccionario.read();
                        caracter = (char) valor;
                    }
                    contCaracter = 0;
                }

                if (longPalabra != 0) {
                    for (int i = 0; (i != letrasAleatorias.length) && frecLetra; i++) {
                        if (tf_nueva.letraTf(i) > letras.letraTf(i)) {
                            frecLetra = false;
                        }
                    }
                    if (frecLetra) {
                        System.out.println(pDiccionario);
                        contImp = 1;
                    }
                    tf_nueva = new TablaFrecuencias();
                    for (int y = 0; y < abecedario.length; y++) {
                        tf_nueva.actualizarFrecuencia(abecedario[y]);

                    }
                }
                pDiccionario = "";
                contCaracter = 0;
                longPalabra = 0;
                frecLetra = true;
                valor = diccionario.read();
                caracter = (char) valor;
            }
            if (contImp == 1) {
                partida.setComodin();
            } else {
                System.out.println(' ');
                System.out.println("No hay palabras, se te ha devuelto el comodín.");
            }
            diccionario.close();

        } else {
            if (pAnterior.LongPalabra() != 0) {
                for (int i = 0; i < pAnterior.LongPalabra(); i++) {
                    tf_ant.actualizarFrecuencia(pAnterior.letra(i));
                }
                int valor = diccionario.read();
                char caracter = (char) valor;
                while (valor != -1) {
                    while (caracter != '\n') {
                        while (contCaracter < letrasAleatorias.length && caracter != letrasAleatorias[contCaracter]) {
                            contCaracter++;
                        }
                        if (contCaracter != letrasAleatorias.length) {
                            pDiccionario = pDiccionario + String.valueOf(caracter);
                            tf_nueva.actualizarFrecuencia(caracter);
                            longPalabra++;
                        } else {
                            pDiccionario = "";
                            longPalabra = 0;
                            while (caracter != '\n') {
                                valor = diccionario.read();
                                caracter = (char) valor;
                            }
                        }
                        if (caracter != '\n') {
                            valor = diccionario.read();
                            caracter = (char) valor;
                        }
                        contCaracter = 0;
                    }
                    if (longPalabra != 0) {
                        for (int i = 0; i < partida.numPalabras() && !enCorrectas; i++) {
                            pCorrecta = correctas[i];
                            if (pCorrecta.toString().equals(pDiccionario.toString())) {
                                enCorrectas = true;
                            }
                        }
                        for (int i = 0; (i != letrasAleatorias.length) && frecLetra; i++) {
                            if (tf_nueva.letraTf(i) > letras.letraTf(i)) {
                                frecLetra = false;
                            }
                        }
                        for (int i = 0; i < abecedario.length; i++) {
                            if (tf_ant.letraTf(i) != tf_nueva.letraTf(i)) {
                                contFallo++;
                            }
                        }
                        if (frecLetra && (contFallo == 0 || contFallo == 1 || contFallo == 2) && !enCorrectas && (pAnterior.LongPalabra() == pDiccionario.length() || (pAnterior.LongPalabra() + 1) == pDiccionario.length())) {
                            pAnt = pDiccionario.toString().equals(pAnterior.toString());
                            if (!pAnt) {
                                System.out.println(pDiccionario);
                                contImp = 1;
                            }
                        }
                    }
                    tf_nueva = new TablaFrecuencias();
                    for (int y = 0; y < abecedario.length; y++) {
                        tf_nueva.actualizarFrecuencia(abecedario[y]);
                    }
                    pDiccionario = "";
                    contCaracter = 0;
                    contFallo = 0;
                    longPalabra = 0;
                    frecLetra = true;
                    enCorrectas = false;
                    valor = diccionario.read();
                    caracter = (char) valor;
                }
                if (contImp == 1) {
                    partida.setComodin();
                } else {
                    System.out.println(' ');
                    System.out.println("No hay palabras, se te ha devuelto el comodín.");
                }
            } else {
                System.out.println(' ');
                System.out.println("No hay palabras, se te ha devuelto el comodín.");
            }
            diccionario.close();
        }
    }

    // Métodos de objeto.
    // Puntuaciones.
    public void sumSus() {
        puntuacion = puntuacion + 3;
        System.out.println("Puntuación: Sustituir 3 puntos.");
    }

    public void sumOrden() {
        puntuacion = puntuacion + 5;
        System.out.println("Puntuación: Orden 5 puntos.");
    }

    public void sumOrdenSus() {
        puntuacion = puntuacion + 1;
        System.out.println("Puntuación: Sustituir + Orden 1 punto.");
    }

    public void sumAgLetra() {
        puntuacion = puntuacion + 10;
        System.out.println("Puntuación: Añadir Letra (en palabra existente): 10 puntos.");
    }

    public void sumAgLetraOrd() {
        puntuacion = puntuacion + 5;
        System.out.println("Puntuación: Añadir + Orden: 5 puntos.");
    }

    private void long7(int longitud) {
        int numxLetra = (longitud * 15);
        puntuacion = puntuacion + numxLetra;
        System.out.println("Puntación Adicional: Palabra igual o mayor de 7 letras: " + numxLetra + " (15 puntos por letra).");
    }

    private void mismaLetra() {
        puntuacion = puntuacion + 10;
        System.out.println("Puntación Adicional: palabra formada con mínimo tres letras iguales: 10 puntos.");
    }

    public void palabraCorrecta(Palabra palabra) {
        correctas[palabras] = palabra;
    }

    public void resultado() {
        System.out.println(' ');
        System.out.println("==========================");
        System.out.println("Has finalizado tu partida.");
        if (palabras == 100) {
            puntuacion = puntuacion + 500;
            System.out.println("Has generado 100 palabras. Bonificación: 500 puntos.");
        }
        System.out.println("Número de palabras: " + palabras + ".");
        System.out.println("Puntuación: " + puntuacion + ".");
        System.out.println("Palabras creadas: ");
    }

    public void resPalabraCorrecta() {
        int x;
        System.out.println("==========================");
        if (palabras == 0) {
            System.out.println("Ninguna palabra creada.");
        } else {
            for (int i = 0; i < palabras; i++) {
                x = i + 1;
                System.out.println(x + ". " + correctas[i]);
            }
        }
        System.out.println("==========================");
    }

    public void sumarPalabra() {
        palabras++;
    }

    public int obtPuntuacion() {
        return puntuacion;
    }

    public int numPalabras() {
        return palabras;
    }

    public boolean comodin() {
        return comodin;
    }

    public void setComodin() {
        comodin = false;
    }
}
