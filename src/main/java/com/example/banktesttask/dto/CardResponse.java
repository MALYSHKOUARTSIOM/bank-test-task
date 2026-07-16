package com.example.banktesttask.dto;

import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardResponse {
    private Long id;
    private String cardNumber;
    private Currency currency;
    private CardType cardType;
    private Long clientId;
}
