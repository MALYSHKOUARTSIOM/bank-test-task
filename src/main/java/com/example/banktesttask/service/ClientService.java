package com.example.banktesttask.service;

import com.example.banktesttask.dto.ClientCreateRequest;
import com.example.banktesttask.dto.ClientResponse;
import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import com.example.banktesttask.mapper.ClientMapper;
import com.example.banktesttask.model.Client;
import com.example.banktesttask.repository.ClientRepository;
import com.example.banktesttask.repository.specification.ClientSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponse createClient(ClientCreateRequest request) {
        Client client = Client.builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .status(null)
                .build();

        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponse(savedClient);
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> searchClients(
            String fullName,
            String phoneNumber,
            String email,
            Integer status,
            String cardNumber,
            CardType cardType,
            Currency currency
    ) {
        Specification<Client> spec = ClientSpecifications.searchByFilters(
                fullName, phoneNumber, email, status, cardNumber, cardType, currency
        );

        List<Client> clients = clientRepository.findAll(spec);

        return clients.stream()
                .map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<String> getPhoneNumbersByCardAndCurrency(CardType cardType, Currency currency, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clientRepository.findPhoneNumbersByCardTypeAndCurrency(cardType, currency, pageable);
    }
}
