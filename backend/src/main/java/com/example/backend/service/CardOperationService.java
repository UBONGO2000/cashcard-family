package com.example.backend.service;


import com.example.backend.domain.CardTransaction;
import com.example.backend.domain.CashCard;
import com.example.backend.dto.card.CashCardResponse;
import com.example.backend.dto.transaction.CardTransactionResponse;
import com.example.backend.dto.transaction.DepositRequest;
import com.example.backend.dto.transaction.OperationResponse;
import com.example.backend.dto.transaction.PaymentRequest;
import com.example.backend.dto.transaction.RefundRequest;
import com.example.backend.exception.NotFoundException;
import com.example.backend.repository.CardTransactionRepository;
import com.example.backend.repository.CashCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CardOperationService {

    private final CashCardRepository cashCardRepository;
    private final CardTransactionRepository cardTransactionRepository;

    public CardOperationService(
            CashCardRepository cashCardRepository,
            CardTransactionRepository cardTransactionRepository
    ) {
        this.cashCardRepository = cashCardRepository;
        this.cardTransactionRepository = cardTransactionRepository;
    }

    public OperationResponse deposit(Long cardId, Long ownerId, DepositRequest request) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        card.deposit(request.amount());

        CardTransaction transaction = CardTransaction.deposit(
                card,
                request.amount(),
                request.reason()
        );

        cashCardRepository.save(card);
        cardTransactionRepository.save(transaction);

        return OperationResponse.of(
                CashCardResponse.from(card),
                CardTransactionResponse.from(transaction)
        );
    }

    public OperationResponse pay(Long cardId, Long ownerId, PaymentRequest request) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        card.pay(request.amount());

        CardTransaction transaction = CardTransaction.payment(
                card,
                request.amount(),
                request.merchant()
        );

        cashCardRepository.save(card);
        cardTransactionRepository.save(transaction);

        return OperationResponse.of(
                CashCardResponse.from(card),
                CardTransactionResponse.from(transaction)
        );
    }

    public OperationResponse refund(Long cardId, Long ownerId, RefundRequest request) {
        CashCard card = findCardByIdAndOwner(cardId, ownerId);

        card.refund(request.amount());

        CardTransaction transaction = CardTransaction.refund(
                card,
                request.amount(),
                request.reason()
        );

        cashCardRepository.save(card);
        cardTransactionRepository.save(transaction);

        return OperationResponse.of(
                CashCardResponse.from(card),
                CardTransactionResponse.from(transaction)
        );
    }

    private CashCard findCardByIdAndOwner(Long cardId, Long ownerId) {
        return cashCardRepository.findByIdAndOwnerId(cardId, ownerId)
                .orElseThrow(() -> new NotFoundException("Carte non trouvée avec l'id : " + cardId));
    }
}