package crecepalabra;

import java.io.*;

public class Palabra {

    private static int MAX_LETRAS = 20;
    private static char letra = ' '; // Se inicializa a espacio
    private char letras[];
    private int longitud;

    public Palabra() {
        letras = new char[MAX_LETRAS];
        longitud = 0;
    }

    private static void saltar_espacios() throws IOException {
        while (letra == ' ') {
            letra = (char) System.in.read();
        }
    }

    public static Palabra leer() throws IOException {
        Palabra palabra = new Palabra();
        int pos = 0;
        saltar_espacios();
        while (letra != '\n') {
            palabra.letras[pos] = letra;
            pos++;
            letra = (char) System.in.read();
        }
        letra = ' ';
        palabra.longitud = pos;
        return palabra;
    }

    public char letra(int posicion) {
        return letras[posicion];
    }

    public boolean rendicion() {
        return longitud == 0;
    }

    public boolean es_igual(Palabra palabra) {
        // Si la longitud es distinta son distintas
        if (longitud != palabra.longitud) {
            return false;
        }

        // Si hay una letra distinta son distintas
        for (int i = 0; i < longitud; ++i) {
            if (letras[i] != palabra.letras[i]) {
                return false;
            }
        }

        // Son iguales
        return true;
    }

    static boolean son_iguales(Palabra p1, Palabra p2) {
        return p1.es_igual(p2);
    }

    public int LongPalabra() {
        return longitud;
    }

    public boolean longInicial() {
        return longitud >= 3;
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < longitud; ++i) {
            res += letras[i];
        }
        return res;
    }
}
