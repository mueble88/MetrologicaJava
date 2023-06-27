package com.metrologica.ing.model;

public class Measures {

    private float error;
    private float patron;
    private float equipoH;

    public Measures() {
    }

    public Measures(float error, float patron, float equipoH) {
        this.error = error;
        this.patron = patron;
        this.equipoH = equipoH;
    }

    public float getError() {
        return error;
    }

    public void setError(float error) {
        this.error = error;
    }

    public float getPatron() {
        return patron;
    }

    public void setPatron(float patron) {
        this.patron = patron;
    }

    public float getEquipoH() {
        return equipoH;
    }

    public void setEquipoH(float equipoH) {
        this.equipoH = equipoH;
    }
}
