package com.example.backend.dto.transaction;


import com.example.backend.domain.CardTransaction;
import com.example.backend.domain.TransactionStatus;
import com.example.backend.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record CardTransactionResponse(
        Long id,
        Long cardId,
        TransactionType type,
        TransactionStatus status,
        BigDecimal amount,
        BigDecimal balanceAfter,
        String merchant,
        String reason,
        Instant createdAt
) {
    public static CardTransactionResponse from(CardTransaction transaction) {
        return new CardTransactionResponse(
                transaction.getId(),
                transaction.getCard().getId(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getMerchant(),
                transaction.getReason(),
                transaction.getCreatedAt()
        );
    }
}