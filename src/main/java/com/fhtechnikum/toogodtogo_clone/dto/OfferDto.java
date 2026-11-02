package com.fhtechnikum.toogodtogo_clone.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OfferDto(
        UUID id,
        String title,
        String description,
        BigDecimal price,
        BigDecimal originalValue,
        int quantityAvailable,
        LocalDateTime pickupStart,
        LocalDateTime pickupEnd,
        UUID storeId
) {
}
