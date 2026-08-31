package com.example.backend.service;


import com.example.backend.domain.CashCard;
import com.example.backend.domain.User;
import com.example.backend.dto.card.CashCardResponse;
import com.example.backend.dto.card.CreateCashCardRequest;
import com.example.backend.dto.card.UpdateCashCardRequest;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.CashCardRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CashCardService {

    private final CashCardRepository cashCardRepository;
    private final UserRepository userRepository;

    public CashCardService(CashCardRepository cashCardRepository, UserRepository userRepository) {
        this.cashCardRepository = cashCardRepository;
        this.userRepository = userRepository;
    }

    public CashCardResponse createCard(Long ownerId, CreateCashCardRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Utilisateur non trouvé avec l'id : " + ownerId));

        CashCard card = new CashCard(
                request.name(),
                request.initialBalance(),
                request.spendingLimit(),
                owner
        );

        CashCard savedCard = cashCardRepository.save(card);

        return CashCardResponse.from(savedCard);
    }

    @Transactional(readOnly = true)
    public List<CashCardResponse> getCardsByOwner(Long ownerId) {
        return cashCardRepository.findByOwnerId(ownerId)
                .stream()
                .map(CashCardResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CashCardResponse getCardByIdAndOwner(Long cardId, Long ownerId) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        return CashCardResponse.from(card);
    }

    public CashCardResponse updateCard(Long cardId, Long ownerId, UpdateCashCardRequest request) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        if (request.name() != null && !request.name().isBlank()) {
            card.rename(request.name());
        }

        if (request.spendingLimit() != null) {
            card.updateSpendingLimit(request.spendingLimit());
        }

        return CashCardResponse.from(card);
    }

    public CashCardResponse blockCard(Long cardId, Long ownerId) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        card.block();

        return CashCardResponse.from(card);
    }

    public CashCardResponse unblockCard(Long cardId, Long ownerId) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        card.unblock();

        return CashCardResponse.from(card);
    }

    public CashCardResponse closeCard(Long cardId, Long ownerId) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        card.close();

        return CashCardResponse.from(card);
    }

    private CashCard findCardByIdAndOwner(Long cardId, Long ownerId) {
        return cashCardRepository.findByIdAndOwnerId(cardId, ownerId)
                .orElseThrow(() -> new NotFoundException("Carte non trouvée avec l'id : " + cardId));
    }
}