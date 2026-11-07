package com.fhtechnikum.toogodtogo_clone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// request body for post /stores and put /stores/{id}
public record StoreCreationDto(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 1000) String description,
        @NotBlank String street,
        @NotBlank @Size(min = 4, max = 5) String postalCode,
        @NotBlank String city
) {
}
