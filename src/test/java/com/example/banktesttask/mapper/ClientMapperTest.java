package com.example.banktesttask.mapper;

import com.example.banktesttask.dto.ClientResponse;
import com.example.banktesttask.model.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private final ClientMapper clientMapper = new ClientMapper();

    @Test
    void toResponse_ShouldMapAllFields() {
        Client client = Client.builder()
                .id(10L)
                .fullName("Петров Петр")
                .phoneNumber("+375294445566")
                .email("petrov@example.com")
                .status(1)
                .build();

        ClientResponse response = clientMapper.toResponse(client);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Петров Петр", response.getFullName());
        assertEquals("+375294445566", response.getPhoneNumber());
        assertEquals("petrov@example.com", response.getEmail());
        assertEquals(1, response.getStatus());
    }

    @Test
    void toResponse_NullClient_ShouldReturnNull() {
        assertNull(clientMapper.toResponse(null));
    }
}
