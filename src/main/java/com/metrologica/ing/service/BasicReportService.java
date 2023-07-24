package com.metrologica.ing.service;

import com.metrologica.ing.model.BasicReport;
import com.metrologica.ing.model.Client;
import com.metrologica.ing.repository.BasicReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<BasicReport> findBasicReportWithSorting(String field, String sort){
        List<BasicReport> reports;
        String desc = "desc";
        if(sort.equals(desc)){
            reports = basicReportRepository.findAll(Sort.by(Sort.Direction.DESC, field));
            return reports;
        }
        reports = basicReportRepository.findAll(Sort.by(Sort.Direction.ASC, field));
        return reports;
    }
}
