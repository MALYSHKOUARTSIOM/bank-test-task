package com.example.banktesttask.controller;

import com.example.banktesttask.dto.ClientCreateRequest;
import com.example.banktesttask.dto.ClientResponse;
import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import com.example.banktesttask.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Клиенты", description = "Управление клиентами банка (создание, динамический поиск, получение телефонов)")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "Завести нового клиента", description = "Регистрирует клиента в системе и возвращает его данные")
    @ApiResponse(responseCode = "200", description = "Клиент успешно создан")
    @ApiResponse(responseCode = "400", description = "Неверный формат входящих данных (не пройдена валидация)")
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientCreateRequest request) {
        ClientResponse response = clientService.createClient(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Динамический поиск клиентов",
            description = "Ищет клиентов по любым совпадениям (ФИО, телефон, email, статус, номер карты, тип карты, валюта). Поиск регистронезависимый и частичный."
    )
    @ApiResponse(responseCode = "200", description = "Результаты поиска успешно получены")
    public ResponseEntity<List<ClientResponse>> searchClients(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) CardType cardType,
            @RequestParam(required = false) Currency currency
    ) {
        List<ClientResponse> response = clientService.searchClients(
                fullName, phoneNumber, email, status, cardNumber, cardType, currency
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/phone-numbers")
    @Operation(
            summary = "Постраничные номера телефонов",
            description = "Возвращает страницу с уникальными номерами телефонов клиентов, отфильтрованных по типу и валюте их карт."
    )
    @ApiResponse(responseCode = "200", description = "Страница с телефонами успешно получена")
    public ResponseEntity<Page<String>> getPhoneNumbers(
            @RequestParam CardType cardType,
            @RequestParam Currency currency,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<String> response = clientService.getPhoneNumbersByCardAndCurrency(cardType, currency, page, size);
        return ResponseEntity.ok(response);
    }
}
