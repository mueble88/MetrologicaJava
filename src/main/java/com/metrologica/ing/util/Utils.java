package com.metrologica.ing.util;

public class Utils {

    public static double calculateAverage(double[] array){

        double totalProm = 0.0;
        double sum = 0.0;

        for(int i = 0; i < array.length; i++){
            sum = sum + array[i];
        }
        int length = array.length;
        totalProm = sum / length;

        int decimales = 2; // Número de decimales que deseas mantener
        double resultado = Math.round(totalProm * Math.pow(10, decimales)) / Math.pow(10, decimales);

        return resultado;
    }

    public static double calculateStandardDeviation(double[] array) {

        // get the sum of array
        double sum = 0.0;
        for (double i : array) {
            sum += i;
        }

        int length = array.length;
        double mean = sum / length;

        // calculate the standard deviation
        double standardDeviation = 0.0;
        for (double num : array) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        double totalProm = Math.sqrt(standardDeviation / length);

        int decimales = 2; // Número de decimales que deseas mantener
        double resultado = Math.round(totalProm * Math.pow(10, decimales)) / Math.pow(10, decimales);
        return resultado;
    }

    public static double calculateIncertA(double standardDeviation) {
        double raiz = Math.sqrt(10);
        double prom = standardDeviation/raiz;
        int decimales = 2; // Número de decimales que deseas mantener
        double resultado = Math.round(prom * Math.pow(10, decimales)) / Math.pow(10, decimales);
        return resultado;
    }

    public static double calculateIncertB1() {
        double number = 0.1;
        double raiz = Math.sqrt(3);
        double prom = number/raiz;
        int decimales = 2; // Número de decimales que deseas mantener
        double resultado = Math.round(prom * Math.pow(10, decimales)) / Math.pow(10, decimales);
        return resultado;
    }

    public static double calculateIncertB2() {
        int numerador = 1;
        int denominador = 2;
        double number = 0.1;
        double decimal = (double) numerador / denominador;
        double raiz = Math.sqrt(3);
        double prom = decimal*(number/raiz);
        int decimales = 2; // Número de decimales que deseas mantener
        double resultado = Math.round(prom * Math.pow(10, decimales)) / Math.pow(10, decimales);
        return resultado;
    }

    public static double calculateK() {
        double numero = 2;
        return numero;
    }

    public static double calculateIncertConb(double incertA, double incertB1, double incertB2) {

        double resultIncertA = incertA*incertA;
        double resultIncertB1 = incertB1*incertB1;
        double resulltIncertB2 = incertB2*incertB2;
        double resultSuma = resultIncertA+resultIncertB1+resulltIncertB2;
        double raiz = Math.sqrt(resultSuma);
        int decimales = 2;
        double resultado = Math.round(raiz * Math.pow(10, decimales)) / Math.pow(10, decimales);
        return resultado;

    }

    public static double calculateIncetExpandida(double k, double incertConb) {
        double result = k*incertConb;
        int decimales = 2;
        double resultado = Math.round(result * Math.pow(10, decimales)) / Math.pow(10, decimales);
        return resultado;
    }



}
