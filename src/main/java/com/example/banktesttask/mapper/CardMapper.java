package com.example.banktesttask.mapper;

import com.example.banktesttask.dto.CardResponse;
import com.example.banktesttask.model.PaymentCard;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardResponse toResponse(PaymentCard card) {
        if (card == null) return null;
        return CardResponse.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .currency(card.getCurrency())
                .cardType(card.getCardType())
                .clientId(card.getClient().getId())
                .build();
    }
}
