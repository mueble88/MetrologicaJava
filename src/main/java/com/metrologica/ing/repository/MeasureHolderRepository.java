package com.metrologica.ing.repository;

import com.metrologica.ing.model.MeasureHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureHolderRepository extends JpaRepository<MeasureHolder, Long> {

}
