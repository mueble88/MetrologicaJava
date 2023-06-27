package com.metrologica.ing.dto;

import com.metrologica.ing.model.Measures;

public class HumedInDto {

    private Measures[] measures;

    public HumedInDto() {
    }

    public HumedInDto(Measures[] measures) {
        this.measures = measures;
    }

    public Measures[] getMeasures() {
        return measures;
    }

    public void setMeasures(Measures[] measures) {
        this.measures = measures;
    }
}
