package com.example.banktesttask.service;

import com.example.banktesttask.dto.CardCreateRequest;
import com.example.banktesttask.dto.CardResponse;
import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import com.example.banktesttask.mapper.CardMapper;
import com.example.banktesttask.model.Client;
import com.example.banktesttask.model.PaymentCard;
import com.example.banktesttask.repository.ClientRepository;
import com.example.banktesttask.repository.PaymentCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCardServiceTest {

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private PaymentCardService paymentCardService;

    private Client client;
    private PaymentCard card;
    private CardResponse cardResponse;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .fullName("Иван Иванов")
                .phoneNumber("+375291112233")
                .email("ivan@example.com")
                .status(null)
                .build();

        card = PaymentCard.builder()
                .id(1L)
                .cardNumber("1111222233334444")
                .currency(Currency.BYN)
                .cardType(CardType.Classic)
                .client(client)
                .build();

        cardResponse = CardResponse.builder()
                .id(1L)
                .cardNumber("1111222233334444")
                .currency(Currency.BYN)
                .cardType(CardType.Classic)
                .clientId(1L)
                .build();
    }

    @Test
    void createCard_Success_ShouldReturnCardResponseAndSetStatus() {
        CardCreateRequest request = CardCreateRequest.builder()
                .cardNumber("1111222233334444")
                .currency(Currency.BYN)
                .cardType(CardType.Classic)
                .clientId(1L)
                .build();

        when(paymentCardRepository.existsByCardNumber(request.getCardNumber())).thenReturn(false);
        when(clientRepository.findById(request.getClientId())).thenReturn(Optional.of(client));
        when(paymentCardRepository.save(any(PaymentCard.class))).thenReturn(card);
        when(cardMapper.toResponse(any(PaymentCard.class))).thenReturn(cardResponse);

        CardResponse result = paymentCardService.createCard(request);

        assertNotNull(result);
        assertEquals("1111222233334444", result.getCardNumber());
        assertEquals(0, client.getStatus());

        verify(paymentCardRepository, times(1)).existsByCardNumber(anyString());
        verify(clientRepository, times(1)).findById(eq(1L));
        verify(paymentCardRepository, times(1)).save(any(PaymentCard.class));
    }

    @Test
    void createCard_DuplicateCardNumber_ShouldThrowException() {
        CardCreateRequest request = CardCreateRequest.builder()
                .cardNumber("1111222233334444")
                .currency(Currency.BYN)
                .cardType(CardType.Classic)
                .clientId(1L)
                .build();

        when(paymentCardRepository.existsByCardNumber(request.getCardNumber())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentCardService.createCard(request);
        });

        assertEquals("Карта с номером 1111222233334444 уже существует", exception.getMessage());

        verify(paymentCardRepository, times(1)).existsByCardNumber(anyString());
        verify(clientRepository, never()).findById(anyLong());
        verify(paymentCardRepository, never()).save(any(PaymentCard.class));
    }

    @Test
    void createCard_ClientNotFound_ShouldThrowException() {
        CardCreateRequest request = CardCreateRequest.builder()
                .cardNumber("1111222233334444")
                .currency(Currency.BYN)
                .cardType(CardType.Classic)
                .clientId(99L)
                .build();

        when(paymentCardRepository.existsByCardNumber(request.getCardNumber())).thenReturn(false);
        when(clientRepository.findById(request.getClientId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentCardService.createCard(request);
        });

        assertEquals("Клиент с ID 99 не найден", exception.getMessage());

        verify(paymentCardRepository, times(1)).existsByCardNumber(anyString());
        verify(clientRepository, times(1)).findById(eq(99L));
        verify(paymentCardRepository, never()).save(any(PaymentCard.class));
    }
}
