package com.metrologica.ing.model;

public class Measures {

    private double error;
    private double pattern;
    private double equipmentH;

    public Measures() {
    }

    public Measures(double error, double pattern, double equipmentH) {
        this.error = error;
        this.pattern = pattern;
        this.equipmentH = equipmentH;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getPattern() {
        return pattern;
    }

    public void setPattern(double pattern) {
        this.pattern = pattern;
    }

    public double getEquipmentH() {
        return equipmentH;
    }

    public void setEquipmentH(double equipmentH) {
        this.equipmentH = equipmentH;
    }
}
