package com.metrologica.ing.service;

import com.metrologica.ing.model.EquipmentInfo;
import com.metrologica.ing.model.TraceInfo;
import com.metrologica.ing.repository.TranceInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraceInfoService {

    @Autowired
    private TranceInfoRepository tranceInfoRepository;

    public TraceInfo save(TraceInfo traceInfo) {
        return tranceInfoRepository.save(traceInfo);
    }
}
