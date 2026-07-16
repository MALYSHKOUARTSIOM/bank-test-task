package com.example.banktesttask.controller;

import com.example.banktesttask.dto.CardCreateRequest;
import com.example.banktesttask.dto.CardResponse;
import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import com.example.banktesttask.service.PaymentCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentCardService paymentCardService;

    @Test
    @WithMockUser
    void createCard_Success_WithValidData() throws Exception {
        CardCreateRequest request = CardCreateRequest.builder()
                .cardNumber("1111222233334444")
                .currency(Currency.BYN)
                .cardType(CardType.Classic)
                .clientId(1L)
                .build();

        CardResponse response = CardResponse.builder()
                .id(10L)
                .cardNumber("1111222233334444")
                .currency(Currency.BYN)
                .cardType(CardType.Classic)
                .clientId(1L)
                .build();

        when(paymentCardService.createCard(any(CardCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardNumber\":\"1111222233334444\",\"currency\":\"BYN\",\"cardType\":\"Classic\",\"clientId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.cardNumber").value("1111222233334444"))
                .andExpect(jsonPath("$.cardType").value("Classic"));
    }

    @Test
    @WithMockUser
    void createCard_BadRequest_WithInvalidCardNumberLength() throws Exception {
        CardCreateRequest request = CardCreateRequest.builder()
                .cardNumber("1234567890")
                .currency(Currency.BYN)
                .cardType(CardType.Classic)
                .clientId(1L)
                .build();

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardNumber\":\"1234567890\",\"currency\":\"BYN\",\"cardType\":\"Classic\",\"clientId\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cardNumber").value("Номер карты должен состоять ровно из 16 цифр"));
    }

    @Test
    void createCard_Unauthorized_WithoutToken() throws Exception {
        CardCreateRequest request = CardCreateRequest.builder()
                .cardNumber("1111222233334444")
                .currency(Currency.BYN)
                .cardType(CardType.Classic)
                .clientId(1L)
                .build();

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardNumber\":\"1111222233334444\",\"currency\":\"BYN\",\"cardType\":\"Classic\",\"clientId\":1}"))
                .andExpect(status().isUnauthorized());
    }
}
