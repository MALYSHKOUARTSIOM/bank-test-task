package com.example.banktesttask.model;

import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}
