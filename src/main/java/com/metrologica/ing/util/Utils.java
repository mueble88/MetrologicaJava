package com.metrologica.ing.util;

import com.metrologica.ing.dto.TemOutDto;
import com.metrologica.ing.model.Measures;

import java.util.List;

public class Utils {

    public static double calculateAverage(double[] array){

        double totalProm = 0.0;
        double sum = 0.0;

        for(int i = 0; i < array.length; i++){
            sum = sum + array[i];
        }
        int length = array.length;
        totalProm = sum / length;

        return totalProm;
    }

    public static double calculateStandardDeviation(double[] array) {

        // get the sum of array
        double sum = 0.0;
        for (double i : array) {
            sum += i;
        }

        // get the mean of array
        int length = array.length;
        double mean = sum / length;

        // calculate the standard deviation
        double standardDeviation = 0.0;
        for (double num : array) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }
}
