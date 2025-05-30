package com.softeams.poSystem.security.dto;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String username,
        String role,
        LocalDateTime createdAt
) {
}
