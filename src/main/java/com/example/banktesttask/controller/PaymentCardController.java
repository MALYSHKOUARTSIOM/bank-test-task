package com.example.banktesttask.controller;

import com.example.banktesttask.dto.CardCreateRequest;
import com.example.banktesttask.dto.CardResponse;
import com.example.banktesttask.service.PaymentCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Карты", description = "Управление банковскими картами (выпуск карт, привязка к клиентам)")
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @PostMapping
    @Operation(
            summary = "Оформить (создать) новую карту",
            description = "Создает карту и привязывает её к клиенту. Автоматически обновляет статус клиента: Classic -> 0, Gold -> 1, Platinum -> 2."
    )
    @ApiResponse(responseCode = "200", description = "Карта успешно создана, статус клиента обновлен")
    @ApiResponse(responseCode = "400", description = "Неверный формат входящих данных или номер карты уже существует")
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardCreateRequest request) {
        CardResponse response = paymentCardService.createCard(request);
        return ResponseEntity.ok(response);
    }
}
