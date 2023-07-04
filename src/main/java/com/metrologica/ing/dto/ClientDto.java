package com.metrologica.ing.dto;

import com.metrologica.ing.model.City;
import com.metrologica.ing.model.Client;
import com.metrologica.ing.model.User;

import javax.persistence.*;

public class ClientDto {

    private long id;

    private String name;

    private String phone;

    private String address;

    private String email;

    private String nit;

    private City city;

    public ClientDto(){}

    public ClientDto(long id, String name,String phone, String address, String email, String nit, City city) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.nit = nit;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public static ClientDto fromClient(Client client){
        ClientDto clientDto= new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setPhone(client.getPhone());
        clientDto.setAddress(client.getAddress());
        clientDto.setEmail(client.getEmail());
        clientDto.setNit(client.getNit());
        clientDto.setCity(client.getCity());

        return clientDto;
    }

}
