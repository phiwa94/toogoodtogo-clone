package com.fhtechnikum.toogodtogo_clone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Request-Body fuer POST /stores und PUT /stores/{id}
public record StoreCreationDto(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 1000) String description,
        @NotBlank String street,
        @NotBlank @Pattern(regexp = "\\d{4,5}", message = "must be 4 or 5 digits") String postalCode,
        @NotBlank String city
) {
}
