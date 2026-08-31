package com.example.backend.controller;


import com.example.backend.dto.card.CashCardResponse;
import com.example.backend.dto.card.CreateCashCardRequest;
import com.example.backend.dto.card.UpdateCashCardRequest;
import com.example.backend.service.CashCardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/cards")
public class CashCardController {

    private final CashCardService cashCardService;

    public CashCardController(CashCardService cashCardService) {
        this.cashCardService = cashCardService;
    }

    @PostMapping
    public ResponseEntity<CashCardResponse> createCard(
            @PathVariable Long userId,
            @Valid @RequestBody CreateCashCardRequest request
    ) {
        CashCardResponse response = cashCardService.createCard(userId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CashCardResponse>> getCards(@PathVariable Long userId) {
        List<CashCardResponse> responses = cashCardService.getCardsByOwner(userId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CashCardResponse> getCard(
            @PathVariable Long userId,
            @PathVariable Long cardId
    ) {
        CashCardResponse response = cashCardService.getCardByIdAndOwner(cardId, userId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<CashCardResponse> updateCard(
            @PathVariable Long userId,
            @PathVariable Long cardId,
            @Valid @RequestBody UpdateCashCardRequest request
    ) {
        CashCardResponse response = cashCardService.updateCard(cardId, userId, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{cardId}/block")
    public ResponseEntity<CashCardResponse> blockCard(
            @PathVariable Long userId,
            @PathVariable Long cardId
    ) {
        CashCardResponse response = cashCardService.blockCard(cardId, userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{cardId}/unblock")
    public ResponseEntity<CashCardResponse> unblockCard(
            @PathVariable Long userId,
            @PathVariable Long cardId
    ) {
        CashCardResponse response = cashCardService.unblockCard(cardId, userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{cardId}/close")
    public ResponseEntity<CashCardResponse> closeCard(
            @PathVariable Long userId,
            @PathVariable Long cardId
    ) {
        CashCardResponse response = cashCardService.closeCard(cardId, userId);

        return ResponseEntity.ok(response);
    }
}