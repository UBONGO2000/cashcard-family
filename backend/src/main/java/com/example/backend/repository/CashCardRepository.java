package com.example.backend.repository;


import com.example.backend.domain.CashCard;
import com.example.backend.domain.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashCardRepository extends JpaRepository<CashCard, Long>, JpaSpecificationExecutor<CashCard> {

    List<CashCard> findByOwnerId(Long ownerId);

    Optional<CashCard> findByIdAndOwnerId(Long id, Long ownerId);

    List<CashCard> findByOwnerIdAndStatus(Long ownerId, CardStatus status);

    boolean existsByIdAndOwnerId(Long id, Long ownerId);
}