package com.fhtechnikum.toogodtogo_clone.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Request-Body fuer POST /offers und PUT /offers/{id}
public record OfferCreationDto(
        @NotBlank @Size(max = 100) String title,
        @Size(max = 1000) String description,
        @NotNull @Positive BigDecimal price,
        @NotNull @Positive BigDecimal originalValue,
        @PositiveOrZero int quantityAvailable,
        @NotNull @Future LocalDateTime pickupStart,
        @NotNull @Future LocalDateTime pickupEnd,
        @NotNull UUID storeId
) {
}
