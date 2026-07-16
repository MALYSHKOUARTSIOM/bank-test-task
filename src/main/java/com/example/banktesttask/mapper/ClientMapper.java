package com.example.banktesttask.mapper;

import com.example.banktesttask.dto.ClientResponse;
import com.example.banktesttask.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResponse toResponse(Client client) {
        if (client == null) return null;
        return ClientResponse.builder()
                .id(client.getId())
                .fullName(client.getFullName())
                .phoneNumber(client.getPhoneNumber())
                .email(client.getEmail())
                .status(client.getStatus())
                .build();
    }
}
