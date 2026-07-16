package com.example.banktesttask.repository;

import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import com.example.banktesttask.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    @Query("SELECT DISTINCT c.phoneNumber FROM Client c " +
           "JOIN c.cards pc " +
           "WHERE pc.cardType = :cardType AND pc.currency = :currency")
    Page<String> findPhoneNumbersByCardTypeAndCurrency(@Param("cardType") CardType cardType, @Param("currency") Currency currency, Pageable pageable);
}
