package com.metrologica.ing.repository;

import com.metrologica.ing.model.ReportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


public interface ReportFileRepository {

//    List<ReportFile> findById(UUID id);

//    @Modifying
//    @Transactional
//    @Query("UPDATE ReportFile report_files SET report_files.id = :id WHERE report_files.id = :id")
//    int updatePictureFields( @Param("id") long id);

}
