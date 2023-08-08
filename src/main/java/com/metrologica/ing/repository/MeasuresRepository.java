package com.metrologica.ing.repository;

import com.metrologica.ing.model.Measures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasuresRepository extends JpaRepository<Measures, Long> {
}
