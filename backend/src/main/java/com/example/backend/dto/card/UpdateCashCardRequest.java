package com.example.backend.dto.card;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateCashCardRequest(

        @Size(max = 120, message = "Le nom de la carte ne peut pas dépasser 120 caractères")
        String name,

        @DecimalMin(value = "0.00", message = "Le plafond ne peut pas être négatif")
        @Digits(integer = 17, fraction = 2, message = "Le plafond doit être un montant valide")
        BigDecimal spendingLimit
) {
}