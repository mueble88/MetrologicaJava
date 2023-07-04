package com.metrologica.ing.util;

public class Utils {

    public static double calculateAverage(double[] array){

        double totalAverage = 0.0;
        double sum = 0.0;

        for(int i = 0; i < array.length; i++){
            sum = sum + array[i];
        }
        int length = array.length;
        totalAverage = sum / length;

        int decimals = 2; // Número de decimales que deseas mantener
        double result = Math.round(totalAverage * Math.pow(10, decimals)) / Math.pow(10, decimals);

        return result;
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

        double totalAverage = Math.sqrt(standardDeviation / length);

        int decimals = 2;
        double result = Math.round(totalAverage * Math.pow(10, decimals)) / Math.pow(10, decimals);
        return result;
    }

    public static double calculateIncertA(double standardDeviation) {
        double raiz = Math.sqrt(10);
        double average = standardDeviation/raiz;
        int decimals = 2;
        double result = Math.round(average * Math.pow(10, decimals)) / Math.pow(10, decimals);
        return result;
    }

    public static double calculateIncertB1() {
        double number = 0.1;
        double root = Math.sqrt(3);
        double average = number/root;
        int decimals = 2;
        double result = Math.round(average * Math.pow(10, decimals)) / Math.pow(10, decimals);
        return result;
    }

    public static double calculateIncertB2() {
        int numerator = 1;
        int denominator = 2;
        double number = 0.1;
        double decimal = (double) numerator / denominator;
        double root = Math.sqrt(3);
        double average = decimal*(number/root);
        int decimals = 2;
        double result = Math.round(average * Math.pow(10, decimals)) / Math.pow(10, decimals);
        return result;
    }

    public static double calculateK() {
        double number = 2;
        return number;
    }

    public static double calculateIncertConb(double incertA, double incertB1, double incertB2) {

        double resultIncertA = incertA*incertA;
        double resultIncertB1 = incertB1*incertB1;
        double resultIncertB2 = incertB2*incertB2;
        double resultSum = resultIncertA+resultIncertB1+resultIncertB2;
        double root = Math.sqrt(resultSum);
        int decimals = 2;
        double result = Math.round(root * Math.pow(10, decimals)) / Math.pow(10, decimals);
        return result;

    }

    public static double calculateIncetExpandida(double k, double incertConb) {
        double result1 = k*incertConb;
        int decimals = 2;
        double result2 = Math.round(result1 * Math.pow(10, decimals)) / Math.pow(10, decimals);
        return result2;
    }



}
