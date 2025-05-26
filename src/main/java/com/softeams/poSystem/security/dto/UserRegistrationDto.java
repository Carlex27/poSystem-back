package com.softeams.poSystem.security.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserRegistrationDto(
        @NotEmpty(message = "User Name must not be empty")
        String userName,

        @NotEmpty(message = "User password must not be empty")
        String userPassword,

        @NotEmpty(message = "User role must not be empty")
        String userRole

) {
}
