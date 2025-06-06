package com.softeams.poSystem.security.services;


import com.softeams.poSystem.security.config.jwtAuth.JwtTokenGenerator;
import com.softeams.poSystem.security.dto.AuthResponseDto;
import com.softeams.poSystem.security.dto.TokenType;
import com.softeams.poSystem.security.dto.UserRegistrationDto;
import com.softeams.poSystem.security.entities.RefreshTokenEntity;
import com.softeams.poSystem.security.entities.User;
import com.softeams.poSystem.security.mapper.UserMapper;
import com.softeams.poSystem.security.repositories.RefreshTokenRepo;
import com.softeams.poSystem.security.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserMapper userMapper;

    @Value("${jwt.expiration-time.access-token}")
    private int accessTokenExpiry;

    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
        try {
            var userInfoEntity = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> {
                        log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                    });

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            createRefreshTokenCookie(response, refreshToken);

            saveUserRefreshToken(userInfoEntity, refreshToken);
            log.info("[AuthService:userSignInAuth] Access token for user: {}, has been generated", authentication.getName());
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(accessTokenExpiry)
                    .userName(userInfoEntity.getUsername())
                    .userId(userInfoEntity.getId())
                    .tokenType(TokenType.Bearer)
                    .build();
        } catch (Exception e) {
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }

    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {

        if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        User userInfoEntity = refreshTokenEntity.getUser();

        //Now create the Authentication object
        Authentication authentication =  createAuthenticationObject(userInfoEntity);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return  AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(userInfoEntity.getUsername())
                .tokenType(TokenType.Bearer)
                .build();
    }

    public AuthResponseDto registerUser(UserRegistrationDto userRegistrationDto, HttpServletResponse httpServletResponse){

        try{
            log.info("[AuthService:registerUser]User Registration Started with :::{}", userRegistrationDto.userName());

            Optional<User> user = userRepository.findByUsername(userRegistrationDto.userName());
            if(user.isPresent()){
                throw new Exception("User Already Exist");
            }

            User userDetailsEntity = userMapper.convertToEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            User savedUserDetails = userRepository.save(userDetailsEntity);
            saveUserRefreshToken(userDetailsEntity,refreshToken);

            createRefreshTokenCookie(httpServletResponse,refreshToken);

            log.info("[AuthService:registerUser] User:{} Successfully registered",savedUserDetails.getUsername());
            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserDetails.getUsername())
                    .userId(savedUserDetails.getId())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }

    }

    private static Authentication createAuthenticationObject(User userInfoEntity) {
        // Extract user details from UserDetailsEntity
        String username = userInfoEntity.getUsername();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }


    private void saveUserRefreshToken(User userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }

    private Cookie createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }
}
