package com.example.banktesttask.service;

import com.example.banktesttask.dto.CardCreateRequest;
import com.example.banktesttask.dto.CardResponse;
import com.example.banktesttask.mapper.CardMapper;
import com.example.banktesttask.model.Client;
import com.example.banktesttask.model.PaymentCard;
import com.example.banktesttask.repository.ClientRepository;
import com.example.banktesttask.repository.PaymentCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentCardService {

    private final PaymentCardRepository paymentCardRepository;
    private final ClientRepository clientRepository;
    private final CardMapper cardMapper;

    @Transactional
    public CardResponse createCard(CardCreateRequest request) {
        if (paymentCardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new IllegalArgumentException("Карта с номером " + request.getCardNumber() + " уже существует");
        }

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + request.getClientId() + " не найден"));

        PaymentCard card = PaymentCard.builder()
                .cardNumber(request.getCardNumber())
                .currency(request.getCurrency())
                .cardType(request.getCardType())
                .client(client)
                .build();

        int newStatus = request.getCardType().getStatusValue();
        if (client.getStatus() == null || newStatus > client.getStatus()) {
            client.setStatus(newStatus);
        }

        PaymentCard savedCard = paymentCardRepository.save(card);

        return cardMapper.toResponse(savedCard);
    }
}
