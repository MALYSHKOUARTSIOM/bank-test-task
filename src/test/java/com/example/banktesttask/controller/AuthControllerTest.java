package com.example.banktesttask.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getJwtToken_Success_WithValidCredentials() throws Exception {
        String credentials = "admin:admin";
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        mockMvc.perform(post("/api/auth/token")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Credentials))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    void getJwtToken_Unauthorized_WithInvalidCredentials() throws Exception {
        String credentials = "admin:wrongpassword";
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        mockMvc.perform(post("/api/auth/token")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Credentials))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getJwtToken_Unauthorized_WithoutCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/token"))
                .andExpect(status().isUnauthorized());
    }
}
