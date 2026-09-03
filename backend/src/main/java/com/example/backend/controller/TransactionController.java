package com.example.backend.controller;



import com.example.backend.dto.transaction.CardTransactionResponse;
import com.example.backend.security.CurrentUserService;
import com.example.backend.service.TransactionQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards/{cardId}/transactions")
public class TransactionController {

    private final TransactionQueryService transactionQueryService;
    private final CurrentUserService currentUserService;

    public TransactionController(
            TransactionQueryService transactionQueryService,
            CurrentUserService currentUserService
    ) {
        this.transactionQueryService = transactionQueryService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<Page<CardTransactionResponse>> getTransactions(
            @PathVariable Long cardId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(
                transactionQueryService.getTransactions(cardId, userId, pageable)
        );
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<CardTransactionResponse> getTransaction(
            @PathVariable Long cardId,
            @PathVariable Long transactionId
    ) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(
                transactionQueryService.getTransaction(cardId, transactionId, userId)
        );
    }
}