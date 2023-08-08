package com.metrologica.ing.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="measures")
@EntityListeners(AuditingEntityListener.class)
public class Measures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double error;

    private double pattern;

    private double equipmentH;

    @ManyToOne
    private MeasureHolder measureHolder;

    public Measures() {
    }

    public Measures(long id, double error, double pattern, double equipmentH, MeasureHolder measureHolder) {
        this.id = id;
        this.error = error;
        this.pattern = pattern;
        this.equipmentH = equipmentH;
        this.measureHolder = measureHolder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public MeasureHolder getMeasureHolder() {
        return measureHolder;
    }

    public void setMeasureHolder(MeasureHolder measureHolder) {
        this.measureHolder = measureHolder;
    }
}
