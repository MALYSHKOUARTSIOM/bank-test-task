package com.example.banktesttask.dto;

import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardCreateRequest {

    @NotBlank(message = "Номер карты не должен быть пустым")
    @Pattern(regexp = "\\d{16}", message = "Номер карты должен состоять ровно из 16 цифр")
    private String cardNumber;

    @NotNull(message = "Валюта карты должна быть указана")
    private Currency currency;

    @NotNull(message = "Тип карты должен быть указан")
    private CardType cardType;

    @NotNull(message = "ID клиента должен быть указан")
    private Long clientId;
}
