package com.example.banktesttask.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "mySuperSecureSecretKeyForBankApp2026MustBeAtLeast32CharsLongForTests");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 3600000L);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        UserDetails userDetails = User.withUsername("admin")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtTokenProvider.generateToken(authentication);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("admin", jwtTokenProvider.getUsernameFromJwt(token));
    }

    @Test
    void validateToken_InvalidSignature_ShouldReturnFalse() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyMTA3MDcyMCwiZXhwIjoxNzIxMDc0MzIwfQ.invalid_signature";
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    void validateToken_MalformedToken_ShouldReturnFalse() {
        assertFalse(jwtTokenProvider.validateToken("not_a_jwt_token"));
    }
}
