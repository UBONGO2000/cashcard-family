package com.example.backend.service;


import com.example.backend.domain.CardTransaction;
import com.example.backend.domain.CashCard;
import com.example.backend.dto.transaction.CardTransactionResponse;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.CardTransactionRepository;
import com.example.backend.repository.CashCardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TransactionQueryService {

    private final CardTransactionRepository cardTransactionRepository;
    private final CashCardRepository cashCardRepository;

    public TransactionQueryService(
            CardTransactionRepository cardTransactionRepository,
            CashCardRepository cashCardRepository
    ) {
        this.cardTransactionRepository = cardTransactionRepository;
        this.cashCardRepository = cashCardRepository;
    }

    public Page<CardTransactionResponse> getTransactions(Long cardId, Long ownerId, Pageable pageable) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        return cardTransactionRepository.findByCardId(card.getId(), pageable)
                .map(CardTransactionResponse::from);
    }

    public CardTransactionResponse getTransaction(Long cardId, Long transactionId, Long ownerId) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        CardTransaction transaction = cardTransactionRepository
                .findByIdAndCardId(transactionId, card.getId())
                .orElseThrow(() -> new NotFoundException("Transaction non trouvée avec l'id : " + transactionId));

        return CardTransactionResponse.from(transaction);
    }

    private CashCard findCardByIdAndOwner(Long cardId, Long ownerId) {
        return cashCardRepository.findByIdAndOwnerId(cardId, ownerId)
                .orElseThrow(() -> new NotFoundException("Carte non trouvée avec l'id : " + cardId));
    }
}