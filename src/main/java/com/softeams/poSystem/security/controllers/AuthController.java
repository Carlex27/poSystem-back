package com.softeams.poSystem.security.controllers;


import com.softeams.poSystem.security.dto.UserRegistrationDto;
import com.softeams.poSystem.security.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Endpoint para iniciar sesion",
            description = "Este endpoint permite a un usuario iniciar sesion",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                        {
                            "access_token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzb2Z0ZWFtcyIsInN1YiI6IkFETUlOMSIsImV4cCI6MTc0MzA4NjU5MywiaWF0IjoxNzQzMDg1NjkzLCJzY29wZSI6IlJFQUQgREVMRVRFIFdSSVRFIn0.b0O076GeUhLDMyrUCxxHEYYA-yXEc2334jlYhIP5SCAwPTYXY1gGHFfNgKnyLGqhB8waMZteqpCgv8QtbKIk-MOj2PsFdm31Jf8y6T742YS3XDzWNS5UkaYWjo8PAmL3zHpIzKlJfRdAfUXED1FE29UBrpwjOHQCs4A_D8pJDL7xzxo9W1g7lru0AiISe-ZuNyiUSf1AvksSflvY2Yr5GUnxmr_NXSE-JdTDGIC2WoTXB_FeWv6U2cqqR5giksUtLErdGsIcLYtpnTfb8J6B0L3D2qyzvnlNl43vdM1VOprsc3_aJp0rRHZumVcs-2i2N2Br9pebpctiwMwcSP_gbQ",
                            "access_token_expiry": 3600,
                            "refresh_token": "Bearer",
                            "user_name": "ADMIN1",
                            "user_id": 1
                        }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(
            Authentication authentication,
            HttpServletResponse response){
        log.info("[AuthController:authenticateUser]Authentication Process Started for user:{}",authentication.getName());
        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication,response));
    }


    @Operation(
            summary = "Endpoint para obtener un nuevo access_token recibiendo el refresh token",
            description = "Este endpoint permite a un usuario poder obtener otro access_token si se expiro enviando el refresh_token en el header Authorization de tipo Bearer",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                        {
                            "access_token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzb2Z0ZWFtcyIsInN1YiI6IkFETUlOMSIsImV4cCI6MTc0MzA4NjczOCwiaWF0IjoxNzQzMDg1ODM4LCJzY29wZSI6IlJFQUQgREVMRVRFIFdSSVRFIn0.Ch_uHl7hPmgfRnrmOgQKJnrFpHNJF8jBv2Zx1XcXmR-A28H7ftw3Yn5g5A4isUXxr-bJJh3FyJZLGqhjOODKrkd6NrjpQFOeSVzdSrP_CFhxQldJC6OT-OixoovqtSmuZYLRMYEx7FHgEHwYXp6-m3tX55LzGiW0_867EEj-n0QODzjS9yvWmAaBYLPHJK9yjl024C-1-py9D4Lr91O3gczHKXEqGQM3vftL7RSwzfFbROk7USNG3OLsW1utmzfDUesTmINk_AG3RxOfWyvGowSMfiR7tjTYgaYCW0toO3DMuJZrN7XgzTvNB1TY8HBCsikLw5crRupFQYnI1cj69Q",
                            "access_token_expiry": 300,
                            "refresh_token": "Bearer",
                            "user_name": "ADMIN1",
                            "user_id": null
                        }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @Operation(
            summary = "Endpoint para crear un usuario",
            description = "Este endpoint permite crear un usuario",
            security = @SecurityRequirement(name = "basicAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                        {
                            "access_token": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzb2Z0ZWFtcyIsInN1YiI6IkFETUlOMSIsImV4cCI6MTc0MzA4NjU5MywiaWF0IjoxNzQzMDg1NjkzLCJzY29wZSI6IlJFQUQgREVMRVRFIFdSSVRFIn0.b0O076GeUhLDMyrUCxxHEYYA-yXEc2334jlYhIP5SCAwPTYXY1gGHFfNgKnyLGqhB8waMZteqpCgv8QtbKIk-MOj2PsFdm31Jf8y6T742YS3XDzWNS5UkaYWjo8PAmL3zHpIzKlJfRdAfUXED1FE29UBrpwjOHQCs4A_D8pJDL7xzxo9W1g7lru0AiISe-ZuNyiUSf1AvksSflvY2Yr5GUnxmr_NXSE-JdTDGIC2WoTXB_FeWv6U2cqqR5giksUtLErdGsIcLYtpnTfb8J6B0L3D2qyzvnlNl43vdM1VOprsc3_aJp0rRHZumVcs-2i2N2Br9pebpctiwMwcSP_gbQ",
                            "access_token_expiry": 3600,
                            "refresh_token": "Bearer",
                            "user_name": "ADMIN1",
                            "user_id": 1
                        }
                    """)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(
            @Valid
            @RequestBody UserRegistrationDto userRegistrationDto,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse){

        log.info("[AuthController:registerUser]Signup Process Started for user:{}",userRegistrationDto.userName());
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser]Errors in user:{}",errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.ok(authService.registerUser(userRegistrationDto,httpServletResponse));
    }

}
