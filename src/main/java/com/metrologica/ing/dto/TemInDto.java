package com.metrologica.ing.dto;

import com.metrologica.ing.model.Measures;

public class TemInDto {

    private Measures[] measures;

    public TemInDto() {
    }

    public TemInDto(Measures[] measures) {
        this.measures = measures;
    }

    public Measures[] getMeasures() {
        return measures;
    }

    public void setMeasures(Measures[] measures) {
        this.measures = measures;
    }
}
