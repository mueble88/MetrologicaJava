package com.metrologica.ing.service;

import com.metrologica.ing.model.EquipmentInfo;
import com.metrologica.ing.repository.EquipmentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentInfoService {

    @Autowired
    private EquipmentInfoRepository equipmentInfoRepository;

    public EquipmentInfo save(EquipmentInfo equipmentInfo) {
        return equipmentInfoRepository.save(equipmentInfo);
    }

}
