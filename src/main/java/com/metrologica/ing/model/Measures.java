package com.metrologica.ing.model;

public class Measures {

    private double error;
    private double patron;
    private double equipoH;

    public Measures() {
    }

    public Measures(double error, double patron, double equipoH) {
        this.error = error;
        this.patron = patron;
        this.equipoH = equipoH;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getPatron() {
        return patron;
    }

    public void setPatron(double patron) {
        this.patron = patron;
    }

    public double getEquipoH() {
        return equipoH;
    }

    public void setEquipoH(double equipoH) {
        this.equipoH = equipoH;
    }
}
