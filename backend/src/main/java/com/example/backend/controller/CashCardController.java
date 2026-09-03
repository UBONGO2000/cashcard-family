package com.example.backend.controller;

import com.example.backend.dto.card.CashCardResponse;
import com.example.backend.dto.card.CreateCashCardRequest;
import com.example.backend.dto.card.UpdateCashCardRequest;
import com.example.backend.security.CurrentUserService;
import com.example.backend.service.CashCardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CashCardController {

    private final CashCardService cashCardService;
    private final CurrentUserService currentUserService;

    public CashCardController(CashCardService cashCardService, CurrentUserService currentUserService) {
        this.cashCardService = cashCardService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<CashCardResponse> createCard(@Valid @RequestBody CreateCashCardRequest request) {
        Long userId = currentUserService.getCurrentUserId();

        CashCardResponse response = cashCardService.createCard(userId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CashCardResponse>> getCards() {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(cashCardService.getCardsByOwner(userId));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CashCardResponse> getCard(@PathVariable Long cardId) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(cashCardService.getCardByIdAndOwner(cardId, userId));
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<CashCardResponse> updateCard(
            @PathVariable Long cardId,
            @Valid @RequestBody UpdateCashCardRequest request
    ) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(cashCardService.updateCard(cardId, userId, request));
    }

    @PostMapping("/{cardId}/block")
    public ResponseEntity<CashCardResponse> blockCard(@PathVariable Long cardId) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(cashCardService.blockCard(cardId, userId));
    }

    @PostMapping("/{cardId}/unblock")
    public ResponseEntity<CashCardResponse> unblockCard(@PathVariable Long cardId) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(cashCardService.unblockCard(cardId, userId));
    }

    @PostMapping("/{cardId}/close")
    public ResponseEntity<CashCardResponse> closeCard(@PathVariable Long cardId) {
        Long userId = currentUserService.getCurrentUserId();

        return ResponseEntity.ok(cashCardService.closeCard(cardId, userId));
    }
}