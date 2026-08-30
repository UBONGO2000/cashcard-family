package com.example.backend.dto.transaction;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PaymentRequest(

        @NotNull(message = "Le montant est obligatoire")
        @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
        @Digits(integer = 17, fraction = 2, message = "Le montant doit être valide")
        BigDecimal amount,

        @NotBlank(message = "Le marchand est obligatoire")
        @Size(max = 255, message = "Le nom du marchand ne peut pas dépasser 255 caractères")
        String merchant
) {
}