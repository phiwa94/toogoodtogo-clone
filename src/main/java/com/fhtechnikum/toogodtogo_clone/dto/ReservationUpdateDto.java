package com.fhtechnikum.toogodtogo_clone.dto;

import com.fhtechnikum.toogodtogo_clone.entity.ReservationStatus;
import jakarta.validation.constraints.NotNull;

// request body for put /reservations/{id}, only the status can be changed
public record ReservationUpdateDto(@NotNull ReservationStatus status) {
}
