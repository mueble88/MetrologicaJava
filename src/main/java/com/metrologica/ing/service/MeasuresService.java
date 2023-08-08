package com.metrologica.ing.service;

import com.metrologica.ing.model.Measures;
import com.metrologica.ing.repository.MeasuresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MeasuresService {

    @Autowired
    private MeasuresRepository measuresRepository;

    public void saveAll(List<Measures> mesures){

        measuresRepository.saveAll(mesures);
    }
}
