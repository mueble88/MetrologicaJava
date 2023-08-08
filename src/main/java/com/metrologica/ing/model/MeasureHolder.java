package com.metrologica.ing.model;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="measure_holder")
@EntityListeners(AuditingEntityListener.class)
public class MeasureHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "measure_holder_id")
    private List<Measures> measures;

    public MeasureHolder() {
    }

    public MeasureHolder(long id, List<Measures> measures) {
        this.id = id;
        this.measures = measures;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Measures> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measures> measures) {
        this.measures = measures;
    }
}
