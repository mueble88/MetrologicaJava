package com.metrologica.ing.controller;

import com.metrologica.ing.model.EquipmentInfo;
import com.metrologica.ing.model.TraceInfo;
import com.metrologica.ing.service.TraceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class TraceInfoController {

    @Autowired
    private TraceInfoService traceInfoService;

    public TraceInfo create(TraceInfo traceInfo) {
        return traceInfoService.create(traceInfo);
    }

}
