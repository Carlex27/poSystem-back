package com.softeams.poSystem.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("access_token_expiry")
    private  int accessTokenExpiry;
    @JsonProperty("refresh_token")
    private TokenType tokenType;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("user_id")
    private Long userId;
}
