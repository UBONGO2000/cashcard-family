package com.example.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@Table(name = "cash_cards")
public class CashCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "spending_limit", precision = 19, scale = 2)
    private BigDecimal spendingLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CardStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected CashCard() {
    }

    public CashCard(String name, BigDecimal initialBalance, BigDecimal spendingLimit, User owner) {
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le solde initial ne peut pas être négatif");
        }
        this.name = name;
        this.balance = initialBalance;
        this.spendingLimit = spendingLimit;
        this.owner = owner;
        this.status = CardStatus.ACTIVE;
        this.version = 0L;
        this.createdAt = Instant.now();
    }

    // Logique métier

    public void deposit(BigDecimal amount) {
        validatePositiveAmount(amount);
        ensureActive();
        this.balance = this.balance.add(amount);
    }

    public void pay(BigDecimal amount) {
        validatePositiveAmount(amount);
        ensureActive();
        ensureSufficientBalance(amount);
        ensureSpendingLimitNotExceeded(amount);
        this.balance = this.balance.subtract(amount);
    }

    public void refund(BigDecimal amount) {
        validatePositiveAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void block() {
        this.status = CardStatus.BLOCKED;
    }

    public void unblock() {
        if (this.status != CardStatus.CLOSED) {
            this.status = CardStatus.ACTIVE;
        }
    }

    public void close() {
        this.status = CardStatus.CLOSED;
    }

    public void updateSpendingLimit(BigDecimal newLimit) {
        if (newLimit != null && newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le plafond ne peut pas être négatif");
        }
        this.spendingLimit = newLimit;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Le nom de la carte est obligatoire");
        }
        if (newName.length() > 120) {
            throw new IllegalArgumentException("Le nom ne peut pas dépasser 120 caractères");
        }
        this.name = newName;
    }


    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
    }

    private void ensureActive() {
        if (this.status != CardStatus.ACTIVE) {
            throw new IllegalStateException("La carte n'est pas active");
        }
    }

    private void ensureSufficientBalance(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Solde insuffisant");
        }
    }

    private void ensureSpendingLimitNotExceeded(BigDecimal amount) {
        if (this.spendingLimit != null && amount.compareTo(this.spendingLimit) > 0) {
            throw new IllegalStateException("Le plafond de dépense est dépassé");
        }
    }


}
