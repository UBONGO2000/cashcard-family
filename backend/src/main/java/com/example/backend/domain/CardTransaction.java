package com.example.backend.domain;


import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@Table(name = "card_transactions")
public class CardTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private CashCard card;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "balance_after", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(length = 255)
    private String merchant;

    @Column(length = 255)
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected CardTransaction() {
    }

    private CardTransaction(
            CashCard card,
            TransactionType type,
            TransactionStatus status,
            BigDecimal amount,
            BigDecimal balanceAfter,
            String merchant,
            String reason
    ) {
        this.card = card;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.merchant = merchant;
        this.reason = reason;
        this.createdAt = Instant.now();
    }


    public static CardTransaction deposit(CashCard card, BigDecimal amount, String reason) {
        return new CardTransaction(
                card,
                TransactionType.DEPOSIT,
                TransactionStatus.COMPLETED,
                amount,
                card.getBalance(),
                null,
                reason
        );
    }

    public static CardTransaction payment(CashCard card, BigDecimal amount, String merchant) {
        return new CardTransaction(
                card,
                TransactionType.PAYMENT,
                TransactionStatus.COMPLETED,
                amount.negate(),
                card.getBalance(),
                merchant,
                null
        );
    }

    public static CardTransaction refund(CashCard card, BigDecimal amount, String reason) {
        return new CardTransaction(
                card,
                TransactionType.REFUND,
                TransactionStatus.COMPLETED,
                amount,
                card.getBalance(),
                null,
                reason
        );
    }

    public static CardTransaction rejectedPayment(
            CashCard card,
            BigDecimal amount,
            String merchant,
            String reason
    ) {
        return new CardTransaction(
                card,
                TransactionType.PAYMENT,
                TransactionStatus.REJECTED,
                amount.negate(),
                card.getBalance(),
                merchant,
                reason
        );
    }


}
