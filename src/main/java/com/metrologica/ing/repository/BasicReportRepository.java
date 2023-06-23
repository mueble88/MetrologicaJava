package com.metrologica.ing.repository;

import com.metrologica.ing.model.BasicReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicReportRepository extends JpaRepository<BasicReport, Long> {

    List<BasicReport> findById(long id);

}
