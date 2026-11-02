package com.fhtechnikum.toogodtogo_clone.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Request-Body fuer POST /reservations
public record ReservationCreationDto(
        @NotNull UUID userId,
        @NotNull UUID offerId,
        @Min(1) @Max(10) int quantity
) {
}
