package com.metrologica.ing.service;

import com.metrologica.ing.model.Client;
import com.metrologica.ing.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findClientWithSorting(String field, String sort){
        List<Client> clients;
        String desc = "desc";
        if(sort.equals(desc)){
            clients = clientRepository.findAll(Sort.by(Sort.Direction.DESC, field));
            return clients;
        }
        clients = clientRepository.findAll(Sort.by(Sort.Direction.ASC, field));
        return clients;
    }

    public List<Client> getClient(long id){
        List<Client> client = clientRepository.findById(id);
        return client;
    }

    public Client getClientById(long id){
        return clientRepository.getClientById(id);
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }


    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public int  updatePublicFields(long idClient,String name, String address,String email,String nit ){

        return clientRepository.updatePublicFields(idClient,name,address,email, nit);
    }


}
