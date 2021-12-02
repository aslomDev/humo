package com.mycompany.myapp.service.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ClientDTO  implements Serializable {

    private String name;

    private String phone;

    private String address;

    @JsonIgnore
    private Set<CardDTO> clients = new HashSet<>();

    public ClientDTO(String name, String phone, String address, Set<CardDTO> clients) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.clients = clients;
    }

    public ClientDTO() {
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

    public Set<CardDTO> getClients() {
        return clients;
    }

    public void setClients(Set<CardDTO> clients) {
        this.clients = clients;
    }

    @Override
    public boolean equals(Object params) {
        if (this == params) return true;
        if (params == null || getClass() != params.getClass()) return false;
        ClientDTO clientDTO = (ClientDTO) params;
        return Objects.equals(name, clientDTO.name) && Objects.equals(phone, clientDTO.phone) && Objects.equals(address, clientDTO.address) && Objects.equals(clients, clientDTO.clients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, address, clients);
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
            "name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            ", address='" + address + '\'' +
            ", clients=" + clients +
            '}';
    }
}
