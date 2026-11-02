package com.fhtechnikum.toogodtogo_clone.dto;

import com.fhtechnikum.toogodtogo_clone.entity.ReservationStatus;
import jakarta.validation.constraints.NotNull;

// Request-Body fuer PUT /reservations/{id} – nur der Status kann geaendert werden
public record ReservationUpdateDto(@NotNull ReservationStatus status) {
}
