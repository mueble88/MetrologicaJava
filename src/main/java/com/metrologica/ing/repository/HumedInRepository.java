package com.metrologica.ing.repository;

import com.metrologica.ing.model.HumedIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumedInRepository extends JpaRepository<HumedIn, Long> {

}
