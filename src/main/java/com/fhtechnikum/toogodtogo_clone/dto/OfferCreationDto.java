package com.fhtechnikum.toogodtogo_clone.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

// request body for post /offers and put /offers/{id}
public record OfferCreationDto(
        @NotBlank @Size(max = 100) String title,
        @Size(max = 1000) String description,
        @Positive double price,
        @Positive double originalValue,
        @PositiveOrZero int quantityAvailable,
        @NotNull @Future LocalDateTime pickupStart,
        @NotNull @Future LocalDateTime pickupEnd,
        @NotNull UUID storeId
) {
}
