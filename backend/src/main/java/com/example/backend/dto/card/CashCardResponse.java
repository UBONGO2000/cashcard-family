package com.example.backend.dto.card;


import com.example.backend.domain.CardStatus;
import com.example.backend.domain.CashCard;

import java.math.BigDecimal;
import java.time.Instant;

public record CashCardResponse(
        Long id,
        String name,
        BigDecimal balance,
        BigDecimal spendingLimit,
        CardStatus status,
        Long ownerId,
        Instant createdAt
) {
    public static CashCardResponse from(CashCard card) {
        return new CashCardResponse(
                card.getId(),
                card.getName(),
                card.getBalance(),
                card.getSpendingLimit(),
                card.getStatus(),
                card.getOwner() != null ? card.getOwner().getId() : null,
                card.getCreatedAt()
        );
    }
}