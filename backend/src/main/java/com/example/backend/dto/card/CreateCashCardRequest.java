package com.example.backend.dto.card;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateCashCardRequest(

        @NotBlank(message = "Le nom de la carte est obligatoire")
        @Size(max = 120, message = "Le nom de la carte ne peut pas dépasser 120 caractères")
        String name,

        @NotNull(message = "Le solde initial est obligatoire")
        @DecimalMin(value = "0.00", message = "Le solde initial ne peut pas être négatif")
        @Digits(integer = 17, fraction = 2, message = "Le solde initial doit être un montant valide")
        BigDecimal initialBalance,

        @DecimalMin(value = "0.00", message = "Le plafond ne peut pas être négatif")
        @Digits(integer = 17, fraction = 2, message = "Le plafond doit être un montant valide")
        BigDecimal spendingLimit
) {
}