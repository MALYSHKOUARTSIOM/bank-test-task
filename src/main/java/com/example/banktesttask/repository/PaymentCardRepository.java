package com.example.banktesttask.repository;

import com.example.banktesttask.model.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    boolean existsByCardNumber(String cardNumber);
}
