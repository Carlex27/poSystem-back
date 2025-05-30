package com.softeams.poSystem.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Schema(description = "Username of the user", example = "user123", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(unique = true, nullable = false)
    private String username;

    @Schema(description = "Password of the user", example = "password", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String password;

    @Schema(description = "Role of the User", example = "ROLE_VENDEDOR / ROLE_ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String roles;

    @Column()
    private LocalDateTime createdAt;

    @Schema(description = "List of refresh tokens", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RefreshTokenEntity> refreshTokens;
}
