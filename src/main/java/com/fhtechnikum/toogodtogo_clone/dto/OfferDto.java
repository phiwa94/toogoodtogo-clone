package com.fhtechnikum.toogodtogo_clone.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferDto(
        UUID id,
        String title,
        String description,
        double price,
        double originalValue,
        int quantityAvailable,
        LocalDateTime pickupStart,
        LocalDateTime pickupEnd,
        UUID storeId
) {
}
