package com.metrologica.ing.service;

import com.metrologica.ing.model.MeasureHolder;
import com.metrologica.ing.repository.MeasureHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasureHolderService {

    @Autowired
    private MeasureHolderRepository measureHolderRepository;

    public void save(MeasureHolder measureHolder){

        measureHolderRepository.save(measureHolder);

    }

}
