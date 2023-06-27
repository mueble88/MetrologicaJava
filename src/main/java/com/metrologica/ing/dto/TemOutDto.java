package com.metrologica.ing.dto;

import com.metrologica.ing.model.Measures;

public class TemOutDto {
    private Measures[] measures;

    public TemOutDto() {
    }

    public TemOutDto(Measures[] measures) {
        this.measures = measures;
    }

    public Measures[] getMeasures() {
        return measures;
    }

    public void setMeasures(Measures[] measures) {
        this.measures = measures;
    }
}
