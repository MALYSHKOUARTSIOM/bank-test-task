package com.example.banktesttask.controller;

import com.example.banktesttask.dto.AuthResponse;
import com.example.banktesttask.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Получение JWT-токена доступа по Basic Auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/token")
    @Operation(
            summary = "Получить JWT-токен",
            description = "Генерирует JWT-токен доступа. Требует прохождения аутентификации через Basic Auth (логин/пароль в заголовке)."
    )
    @ApiResponse(responseCode = "200", description = "Токен успешно сгенерирован")
    @ApiResponse(responseCode = "401", description = "Неверный логин или пароль, либо заголовок Basic Auth отсутствует")
    public ResponseEntity<AuthResponse> getJwtToken(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("Требуется базовая аутентификация (Basic Auth)");
        }

        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
