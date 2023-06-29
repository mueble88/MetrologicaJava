package com.metrologica.ing.repository;

import com.metrologica.ing.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findById(long id);

    @Modifying
    @Transactional
    @Query("UPDATE City city SET city.name = :name WHERE city.id = :id")
    int updateCity( @Param("id") long id,
                            @Param("name")String name);
}
