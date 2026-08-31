package com.example.backend.repository;


import com.example.backend.domain.CardTransaction;
import com.example.backend.domain.TransactionStatus;
import com.example.backend.domain.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long>, JpaSpecificationExecutor<CardTransaction> {

    Page<CardTransaction> findByCardId(Long cardId, Pageable pageable);

    List<CardTransaction> findByCardIdOrderByCreatedAtDesc(Long cardId);

    List<CardTransaction> findByCardIdAndType(Long cardId, TransactionType type);

    List<CardTransaction> findByCardIdAndStatus(Long cardId, TransactionStatus status);

    List<CardTransaction> findByCardIdAndCreatedAtBetween(Long cardId, Instant start, Instant end);

    Optional<CardTransaction> findByIdAndCardId(Long id, Long cardId);

    @Query("SELECT t FROM CardTransaction t WHERE t.card.id = :cardId AND t.createdAt >= :since")
    List<CardTransaction> findRecentTransactions(@Param("cardId") Long cardId, @Param("since") Instant since);
}
