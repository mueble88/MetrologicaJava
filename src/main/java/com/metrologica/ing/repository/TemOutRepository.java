package com.metrologica.ing.repository;

import com.metrologica.ing.model.TemOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemOutRepository extends JpaRepository<TemOut, Long> {
}
