package com.example.backend.controller;


import com.example.backend.dto.transaction.DepositRequest;
import com.example.backend.dto.transaction.OperationResponse;
import com.example.backend.dto.transaction.PaymentRequest;
import com.example.backend.dto.transaction.RefundRequest;
import com.example.backend.security.CurrentUserService;
import com.example.backend.service.CardOperationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards/{cardId}")
public class CardOperationController {

    private final CardOperationService cardOperationService;
    private final CurrentUserService currentUserService;

    public CardOperationController(
            CardOperationService cardOperationService,
            CurrentUserService currentUserService
    ) {
        this.cardOperationService = cardOperationService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/deposits")
    public ResponseEntity<OperationResponse> deposit(
            @PathVariable Long cardId,
            @Valid @RequestBody DepositRequest request
    ) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(cardOperationService.deposit(cardId, userId, request));
    }

    @PostMapping("/payments")
    public ResponseEntity<OperationResponse> pay(
            @PathVariable Long cardId,
            @Valid @RequestBody PaymentRequest request
    ) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(cardOperationService.pay(cardId, userId, request));
    }

    @PostMapping("/refunds")
    public ResponseEntity<OperationResponse> refund(
            @PathVariable Long cardId,
            @Valid @RequestBody RefundRequest request
    ) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(cardOperationService.refund(cardId, userId, request));
    }
}