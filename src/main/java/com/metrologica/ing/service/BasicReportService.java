package com.metrologica.ing.service;

import com.metrologica.ing.model.BasicReport;
import com.metrologica.ing.repository.BasicReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicReportService {

    @Autowired
    private BasicReportRepository basicReportRepository;

    public BasicReport save(BasicReport basicReport) {

        return basicReportRepository.save(basicReport);
    }

    public List<BasicReport> findAll() {
        return basicReportRepository.findAll();
    }

    public List<BasicReport> findAllOrderByIdDesc() {
        return basicReportRepository.findAllByOrderByIdDesc();
    }
}
