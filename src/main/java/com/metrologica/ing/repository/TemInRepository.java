package com.metrologica.ing.repository;

import com.metrologica.ing.model.TemIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemInRepository extends JpaRepository<TemIn, Long> {
}
