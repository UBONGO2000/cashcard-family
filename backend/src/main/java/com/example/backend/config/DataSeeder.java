package com.example.backend.config;

import com.example.backend.domain.User;
import com.example.backend.domain.UserRole;
import com.example.backend.dto.card.CashCardResponse;
import com.example.backend.dto.card.CreateCashCardRequest;
import com.example.backend.dto.transaction.DepositRequest;
import com.example.backend.dto.transaction.PaymentRequest;
import com.example.backend.dto.transaction.RefundRequest;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CardOperationService;
import com.example.backend.service.CashCardService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@Profile("dev")
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CashCardService cashCardService;
    private final CardOperationService cardOperationService;

    public DataSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CashCardService cashCardService,
            CardOperationService cardOperationService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cashCardService = cashCardService;
        this.cardOperationService = cardOperationService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }

        User parent = userRepository.save(
                new User(
                        "parent@test.com",
                        passwordEncoder.encode("password123"),
                        "Parent",
                        "Test",
                        UserRole.PARENT
                )
        );

        User child = userRepository.save(
                new User(
                        "lucas@test.com",
                        passwordEncoder.encode("password123"),
                        "Lucas",
                        "Test",
                        UserRole.CHILD
                )
        );

        CashCardResponse familyCard = cashCardService.createCard(
                parent.getId(),
                new CreateCashCardRequest(
                        "Carte famille",
                        new BigDecimal("50.00"),
                        new BigDecimal("100.00")
                )
        );

        CashCardResponse lucasCard = cashCardService.createCard(
                child.getId(),
                new CreateCashCardRequest(
                        "Carte de Lucas",
                        new BigDecimal("30.00"),
                        new BigDecimal("20.00")
                )
        );

        cardOperationService.deposit(
                familyCard.id(),
                parent.getId(),
                new DepositRequest(
                        new BigDecimal("20.00"),
                        "Rechargement initial"
                )
        );

        cardOperationService.pay(
                familyCard.id(),
                parent.getId(),
                new PaymentRequest(
                        new BigDecimal("12.50"),
                        "Carrefour"
                )
        );

        cardOperationService.deposit(
                lucasCard.id(),
                child.getId(),
                new DepositRequest(
                        new BigDecimal("10.00"),
                        "Argent de poche"
                )
        );

        cardOperationService.pay(
                lucasCard.id(),
                child.getId(),
                new PaymentRequest(
                        new BigDecimal("5.30"),
                        "Boulangerie"
                )
        );

        cardOperationService.refund(
                lucasCard.id(),
                child.getId(),
                new RefundRequest(
                        new BigDecimal("2.00"),
                        "Remboursement boulangerie"
                )
        );
    }
}