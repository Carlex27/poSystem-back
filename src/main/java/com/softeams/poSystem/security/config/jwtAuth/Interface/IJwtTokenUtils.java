package com.softeams.poSystem.security.config.jwtAuth.Interface;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IJwtTokenUtils {
    String getUserName(Jwt jwtToken);
    String getUserNameFromToken(HttpServletRequest request);
}
