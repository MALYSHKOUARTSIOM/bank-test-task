package com.example.banktesttask.service;

import com.example.banktesttask.dto.ClientCreateRequest;
import com.example.banktesttask.dto.ClientResponse;
import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import com.example.banktesttask.mapper.ClientMapper;
import com.example.banktesttask.model.Client;
import com.example.banktesttask.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private ClientResponse clientResponse;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .fullName("Иван Иванов")
                .phoneNumber("+375291112233")
                .email("ivan@example.com")
                .status(null)
                .build();

        clientResponse = ClientResponse.builder()
                .id(1L)
                .fullName("Иван Иванов")
                .phoneNumber("+375291112233")
                .email("ivan@example.com")
                .status(null)
                .build();
    }

    @Test
    void createClient_ShouldReturnClientResponse() {
        ClientCreateRequest request = ClientCreateRequest.builder()
                .fullName("Иван Иванов")
                .phoneNumber("+375291112233")
                .email("ivan@example.com")
                .build();

        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientMapper.toResponse(any(Client.class))).thenReturn(clientResponse);

        ClientResponse result = clientService.createClient(request);

        assertNotNull(result);
        assertEquals("Иван Иванов", result.getFullName());
        assertEquals("+375291112233", result.getPhoneNumber());

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void searchClients_ShouldReturnList() {
        when(clientRepository.findAll(any(Specification.class))).thenReturn(List.of(client));
        when(clientMapper.toResponse(client)).thenReturn(clientResponse);

        List<ClientResponse> results = clientService.searchClients("Иван", null, null, null, null, null, null);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Иван Иванов", results.get(0).getFullName());
        verify(clientRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getPhoneNumbersByCardAndCurrency_ShouldReturnPage() {
        Page<String> phonePage = new PageImpl<>(List.of("+375291112233"));

        when(clientRepository.findPhoneNumbersByCardTypeAndCurrency(any(CardType.class), any(Currency.class), any(Pageable.class)))
                .thenReturn(phonePage);

        Page<String> result = clientService.getPhoneNumbersByCardAndCurrency(CardType.Classic, Currency.BYN, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("+375291112233", result.getContent().get(0));
        verify(clientRepository, times(1))
                .findPhoneNumbersByCardTypeAndCurrency(eq(CardType.Classic), eq(Currency.BYN), any(Pageable.class));
    }
}
