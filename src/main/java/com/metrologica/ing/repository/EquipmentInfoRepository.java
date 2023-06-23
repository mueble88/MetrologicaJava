package com.metrologica.ing.repository;

import com.metrologica.ing.model.EquipmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentInfoRepository extends JpaRepository<EquipmentInfo, Long> {

    List<EquipmentInfo> findById(long id);
}
