package com.example.backend.controller;


import com.example.backend.dto.transaction.CardTransactionResponse;
import com.example.backend.service.TransactionQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/cards/{cardId}/transactions")
public class TransactionController {

    private final TransactionQueryService transactionQueryService;

    public TransactionController(TransactionQueryService transactionQueryService) {
        this.transactionQueryService = transactionQueryService;
    }

    @GetMapping
    public ResponseEntity<Page<CardTransactionResponse>> getTransactions(
            @PathVariable Long userId,
            @PathVariable Long cardId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CardTransactionResponse> response = transactionQueryService.getTransactions(cardId, userId, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<CardTransactionResponse> getTransaction(
            @PathVariable Long userId,
            @PathVariable Long cardId,
            @PathVariable Long transactionId
    ) {
        CardTransactionResponse response = transactionQueryService.getTransaction(
                cardId,
                transactionId,
                userId
        );

        return ResponseEntity.ok(response);
    }
}