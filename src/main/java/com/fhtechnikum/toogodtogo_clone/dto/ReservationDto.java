package com.fhtechnikum.toogodtogo_clone.dto;

import com.fhtechnikum.toogodtogo_clone.entity.ReservationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationDto(
        UUID id,
        UUID userId,
        UUID offerId,
        int quantity,
        LocalDateTime createdAt,
        ReservationStatus status
) {
}
