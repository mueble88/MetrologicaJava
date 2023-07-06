package com.metrologica.ing.repository;

import com.metrologica.ing.model.Picture;
import com.metrologica.ing.model.ReportFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportFilesRepository extends JpaRepository<ReportFiles, UUID> {


}
