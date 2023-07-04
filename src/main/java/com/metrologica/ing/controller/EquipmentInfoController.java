package com.metrologica.ing.controller;

import com.metrologica.ing.model.Client;
import com.metrologica.ing.model.EquipmentInfo;
import com.metrologica.ing.service.EquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class EquipmentInfoController {

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    public EquipmentInfo save(EquipmentInfo equipmentInfo) {
        return equipmentInfoService.save(equipmentInfo);
    }
}
