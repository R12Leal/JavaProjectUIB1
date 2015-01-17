package crecepalabra;

public class TablaFrecuencias {

    private static int MAXIMO = 5;
    private char letras[];
    private int frecuencias[];
    private int num;
    private int tamano;

    TablaFrecuencias() {
        tamano = MAXIMO;
        letras = new char[tamano];
        frecuencias = new int[tamano];
        num = 0;
    }

    private void redimensionar() {
        int nuevoTamano = tamano + 10;
        char nuevaLetras[] = new char[nuevoTamano];
        int nuevaFrecuencias[] = new int[nuevoTamano];

        for (int i = 0; i < num; ++i) {
            nuevaLetras[i] = letras[i];
            nuevaFrecuencias[i] = frecuencias[i];
        }

        tamano = nuevoTamano;
        letras = nuevaLetras;
        frecuencias = nuevaFrecuencias;
    }

    public void actualizarFrecuencia(char c) {
        int n = 0;
        while (n < tamano && n < num && letras[n] != c) {
            n++;
        }

        if (n == tamano) {
            redimensionar();
        }

        if (n == num) {
            letras[n] = c;
            frecuencias[n] = 0;
            num++;
        }

        frecuencias[n]++;
    }

    public int letraTf(int posicion) {
        return frecuencias[posicion];
    }

    public void imprimir() {
        System.out.println("Aparecen " + num + " caracteres distintos.");
        for (int i = 0; i < num; ++i) {
            System.out.println(letras[i] + " " + frecuencias[i]);
        }
    }
}
