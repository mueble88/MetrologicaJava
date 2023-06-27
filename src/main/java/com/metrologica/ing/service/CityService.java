package com.metrologica.ing.service;

import com.metrologica.ing.model.City;
import com.metrologica.ing.model.Client;
import com.metrologica.ing.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public City save(City city) {
        return cityRepository.save(city);
    }

    public int  updateCity(long idClient,String name ){

        return cityRepository.updateCity(idClient,name);
    }

}
