package com.metrologica.ing.repository;

import com.metrologica.ing.model.ReportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportFileRepository extends JpaRepository<ReportFile, UUID> {

//    List<ReportFile> findById(UUID id);


}
