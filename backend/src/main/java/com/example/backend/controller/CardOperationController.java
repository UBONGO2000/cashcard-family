package com.example.backend.controller;


import com.example.backend.dto.transaction.DepositRequest;
import com.example.backend.dto.transaction.OperationResponse;
import com.example.backend.dto.transaction.PaymentRequest;
import com.example.backend.dto.transaction.RefundRequest;
import com.example.backend.service.CardOperationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/cards/{cardId}")
public class CardOperationController {

    private final CardOperationService cardOperationService;

    public CardOperationController(CardOperationService cardOperationService) {
        this.cardOperationService = cardOperationService;
    }

    @PostMapping("/deposits")
    public ResponseEntity<OperationResponse> deposit(
            @PathVariable Long userId,
            @PathVariable Long cardId,
            @Valid @RequestBody DepositRequest request
    ) {
        OperationResponse response = cardOperationService.deposit(cardId, userId, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/payments")
    public ResponseEntity<OperationResponse> pay(
            @PathVariable Long userId,
            @PathVariable Long cardId,
            @Valid @RequestBody PaymentRequest request
    ) {
        OperationResponse response = cardOperationService.pay(cardId, userId, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refunds")
    public ResponseEntity<OperationResponse> refund(
            @PathVariable Long userId,
            @PathVariable Long cardId,
            @Valid @RequestBody RefundRequest request
    ) {
        OperationResponse response = cardOperationService.refund(cardId, userId, request);

        return ResponseEntity.ok(response);
    }
}