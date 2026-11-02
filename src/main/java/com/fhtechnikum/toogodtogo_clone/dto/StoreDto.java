package com.fhtechnikum.toogodtogo_clone.dto;

import java.util.UUID;

public record StoreDto(UUID id, String name, String description, String street, String postalCode, String city) {
}
