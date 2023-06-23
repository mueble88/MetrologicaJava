package com.metrologica.ing.repository;

import com.metrologica.ing.model.TraceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranceInfoRepository extends JpaRepository<TraceInfo, Long> {

    List<TraceInfo> findById(long id);
}
