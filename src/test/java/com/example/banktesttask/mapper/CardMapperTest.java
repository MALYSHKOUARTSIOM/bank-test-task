package com.example.banktesttask.mapper;

import com.example.banktesttask.dto.CardResponse;
import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import com.example.banktesttask.model.Client;
import com.example.banktesttask.model.PaymentCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardMapperTest {

    private final CardMapper cardMapper = new CardMapper();

    @Test
    void toResponse_ShouldMapAllFields() {
        Client client = Client.builder()
                .id(5L)
                .build();

        PaymentCard card = PaymentCard.builder()
                .id(20L)
                .cardNumber("1111222233334444")
                .currency(Currency.EUR)
                .cardType(CardType.Gold)
                .client(client)
                .build();

        CardResponse response = cardMapper.toResponse(card);

        assertNotNull(response);
        assertEquals(20L, response.getId());
        assertEquals("1111222233334444", response.getCardNumber());
        assertEquals(Currency.EUR, response.getCurrency());
        assertEquals(CardType.Gold, response.getCardType());
        assertEquals(5L, response.getClientId());
    }

    @Test
    void toResponse_NullCard_ShouldReturnNull() {
        assertNull(cardMapper.toResponse(null));
    }
}
