package com.example.backend.dto.transaction;


import com.example.backend.dto.card.CashCardResponse;

public record OperationResponse(
        CashCardResponse card,
        CardTransactionResponse transaction
) {
    public static OperationResponse of(CashCardResponse card, CardTransactionResponse transaction) {
        return new OperationResponse(card, transaction);
    }
}