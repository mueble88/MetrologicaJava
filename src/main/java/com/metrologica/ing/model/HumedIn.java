package com.metrologica.ing.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="humed_in")
@EntityListeners(AuditingEntityListener.class)
public class HumedIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Measures[] measures;

    public HumedIn() {
    }

    public HumedIn(long id, Measures[] measures) {
        this.id = id;
        this.measures = measures;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Measures[] getMeasures() {
        return measures;
    }

    public void setMeasures(Measures[] measures) {
        this.measures = measures;
    }
}
