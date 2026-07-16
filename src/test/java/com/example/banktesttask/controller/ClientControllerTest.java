package com.example.banktesttask.controller;

import com.example.banktesttask.dto.ClientCreateRequest;
import com.example.banktesttask.dto.ClientResponse;
import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import com.example.banktesttask.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    @WithMockUser
    void createClient_Success_WithValidData() throws Exception {
        ClientCreateRequest request = ClientCreateRequest.builder()
                .fullName("Иван Иванов")
                .phoneNumber("+375291112233")
                .email("ivanov@example.com")
                .build();

        ClientResponse response = ClientResponse.builder()
                .id(1L)
                .fullName("Иван Иванов")
                .phoneNumber("+375291112233")
                .email("ivanov@example.com")
                .status(null)
                .build();

        when(clientService.createClient(any(ClientCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Иван Иванов\",\"phoneNumber\":\"+375291112233\",\"email\":\"ivanov@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Иван Иванов"));
    }

    @Test
    @WithMockUser
    void createClient_BadRequest_WithInvalidEmail() throws Exception {
        ClientCreateRequest request = ClientCreateRequest.builder()
                .fullName("Иван Иванов")
                .phoneNumber("+375291112233")
                .email("invalid-email")
                .build();

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Иван Иванов\",\"phoneNumber\":\"+375291112233\",\"email\":\"invalid-email\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Некорректный формат email"));
    }

    @Test
    @WithMockUser
    void searchClients_Success() throws Exception {
        ClientResponse response = ClientResponse.builder()
                .id(1L)
                .fullName("Иван Иванов")
                .build();

        when(clientService.searchClients(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/clients/search")
                        .param("fullName", "Иван"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Иван Иванов"));
    }

    @Test
    @WithMockUser
    void getPhoneNumbers_Success() throws Exception {
        Page<String> phonePage = new PageImpl<>(List.of("+375291112233"));

        when(clientService.getPhoneNumbersByCardAndCurrency(any(CardType.class), any(Currency.class), anyInt(), anyInt()))
                .thenReturn(phonePage);

        mockMvc.perform(get("/api/clients/phone-numbers")
                        .param("cardType", "Classic")
                        .param("currency", "BYN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0]").value("+375291112233"));
    }

    @Test
    void searchClients_Unauthorized_WithoutToken() throws Exception {
        mockMvc.perform(get("/api/clients/search")
                        .param("fullName", "Иван"))
                .andExpect(status().isUnauthorized());
    }
}
