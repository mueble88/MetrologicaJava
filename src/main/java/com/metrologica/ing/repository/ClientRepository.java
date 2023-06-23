package com.metrologica.ing.repository;

import com.metrologica.ing.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findById(long id);

    Client getClientById(long id);

    @Modifying
    @Transactional
    @Query("UPDATE Client client SET client.name = :name, client.address = :address, client.email = :email, client.nit = :nit  WHERE client.id = :id")
    int updatePublicFields( @Param("id") long id,
                            @Param("name")String name,
                            @Param("address")String address,
                            @Param("email")String email,
                            @Param("nit")String nit);


}
