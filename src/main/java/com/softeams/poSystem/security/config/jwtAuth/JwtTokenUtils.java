package com.softeams.poSystem.security.config.jwtAuth;


import com.softeams.poSystem.security.config.RSAKeyRecord;
import com.softeams.poSystem.security.config.user.UserConfig;
import com.softeams.poSystem.security.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils{
    private final UserRepository userRepository;
    private final RSAKeyRecord rsaKeyRecord;

    public String getUserName(Jwt jwtToken){
        return jwtToken.getSubject();
    }

    public String getUserNameFromToken(HttpServletRequest request){
        JwtDecoder jwtDecoder =  NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token = authHeader.substring(7);
        final Jwt jwtToken = jwtDecoder.decode(token);
        return getUserName(jwtToken);
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails){
        final String username = getUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = username.equals(userDetails.getUsername());
        return !isTokenExpired && isTokenUserSameAsDatabase;
    }

    private boolean getIfTokenIsExpired(Jwt jwtToken){
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    public UserDetails userDetails(String username){
        return userRepository
                .findByUsername(username)
                .map(UserConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found"));
    }
}
