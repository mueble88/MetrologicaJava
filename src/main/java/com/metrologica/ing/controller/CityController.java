package com.metrologica.ing.controller;

import com.metrologica.ing.dto.APIResponseDto;
import com.metrologica.ing.model.City;
import com.metrologica.ing.model.Client;
import com.metrologica.ing.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class CityController {


    @Autowired
    private CityService cityService;

    @PostMapping("/city")
    public City saveCity(@RequestBody City city) {
        return cityService.save(city);
    }

    @PutMapping("/city/{id}")
    public Object update(@PathVariable long id , @RequestBody City city) {

        City oneCity = new City();
        oneCity.setId(id);
        oneCity.setName(city.getName());

        long idCity = oneCity.getId();
        String name = city.getName();

        int result = cityService.updateCity(idCity,name);
        if(result == 1){
            return oneCity;
        }
        return new ResponseEntity<City>(HttpStatus.BAD_REQUEST);

    }

}
