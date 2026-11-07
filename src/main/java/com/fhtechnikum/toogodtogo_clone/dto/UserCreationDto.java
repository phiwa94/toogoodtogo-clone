package com.fhtechnikum.toogodtogo_clone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// request body for post /users and put /users/{id}
public record UserCreationDto(
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @NotBlank @Email String email
) {
}
