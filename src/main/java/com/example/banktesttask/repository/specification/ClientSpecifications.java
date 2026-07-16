package com.example.banktesttask.repository.specification;

import com.example.banktesttask.enums.CardType;
import com.example.banktesttask.enums.Currency;
import com.example.banktesttask.model.Client;
import com.example.banktesttask.model.PaymentCard;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecifications {

    public static Specification<Client> searchByFilters(
            String fullName,
            String phoneNumber,
            String email,
            Integer status,
            String cardNumber,
            CardType cardType,
            Currency currency
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            query.distinct(true);

            if (fullName != null && !fullName.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("fullName")),
                        "%" + fullName.toLowerCase() + "%"
                ));
            }

            if (phoneNumber != null && !phoneNumber.isBlank()) {
                predicates.add(cb.like(
                        root.get("phoneNumber"),
                        "%" + phoneNumber + "%"
                ));
            }

            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if ((cardNumber != null && !cardNumber.isBlank()) || cardType != null || currency != null) {
                Join<Client, PaymentCard> cardJoin = root.join("cards", JoinType.LEFT);

                if (cardNumber != null && !cardNumber.isBlank()) {
                    predicates.add(cb.like(
                            cardJoin.get("cardNumber"),
                            "%" + cardNumber + "%"
                    ));
                }

                if (cardType != null) {
                    predicates.add(cb.equal(cardJoin.get("cardType"), cardType));
                }

                if (currency != null) {
                    predicates.add(cb.equal(cardJoin.get("currency"), currency));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
