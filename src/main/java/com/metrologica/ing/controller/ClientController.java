package com.metrologica.ing.controller;

import com.metrologica.ing.dto.APIResponseDto;
import com.metrologica.ing.dto.ClientDto;
import com.metrologica.ing.model.City;
import com.metrologica.ing.model.Client;
import com.metrologica.ing.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    private List<Client> getClients(@RequestParam(defaultValue = "name") String field,
                                    @RequestParam(defaultValue = "asc") String sort){
        List<Client> clients = clientService.findClientWithSorting(field, sort);
        return clients;
    }

    @GetMapping(value = "/client/{id}")
    public ClientDto getClient(@PathVariable long id){
        List<Client> client = clientService.getClient(id);
        ClientDto clientDto = new ClientDto();
        if(client != null){
            Client oneClient = client.get(0);
            ClientDto dto = ClientDto.fromClient(oneClient);
            return dto;
        }
        return null;
    }

    @PostMapping("/client")
    public Client createClient(@RequestBody Client client) {
        return clientService.save(client);
    }

    @PutMapping("/client/{id}")
    public Object update(@PathVariable long id , @RequestBody Client client) {

        Client oneClient = new Client();
        oneClient.setId(id);
        oneClient.setName(client.getName());
        oneClient.setPhone(client.getPhone());
        oneClient.setAddress(client.getAddress());
        oneClient.setEmail(client.getEmail());
        oneClient.setNit(client.getNit());
        oneClient.setCity(client.getCity());

        long idClient = oneClient.getId();
        String name = client.getName();
        String phone = client.getPhone();
        String address = client.getAddress();
        String email = client.getEmail();
        String nit = client.getNit();
        City city = client.getCity();

        int result = clientService.updatePublicFields(idClient,name,address,email, nit);
        if(result == 1){
            return oneClient;
        }
        return new ResponseEntity<Client>(HttpStatus.BAD_REQUEST);

    }


}
