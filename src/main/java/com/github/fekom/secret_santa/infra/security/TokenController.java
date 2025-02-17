package com.github.fekom.secret_santa.infra.security;

import com.github.fekom.secret_santa.model.dto.LoginRequest;
import com.github.fekom.secret_santa.model.dto.LoginResponse;
import com.github.fekom.secret_santa.entity.RoleEntity;
import com.github.fekom.secret_santa.model.dto.TokenRefreshRequestDto;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.webauthn.api.PublicKeyCose;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Login", description = "Login Endpoint")
public class TokenController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private  JwtEncoder jwtEncoder;

    private JwtDecoder jwtDecoder;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    public  TokenController() {

    }

    @PostMapping("/api/login")
    @PreAuthorize("hasAuthority('SCOPE_OWNER')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successfully logged in",
            content = {@Content(
                schema = @Schema(implementation = LoginResponse.class),
                mediaType = "application/json"
            )} 
        ),
        @ApiResponse(responseCode = "404", description = "invalid email or password",
            content = {@Content}
        ),
    })
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest loginRequest) {

        var user = userRepository.findByEmail(loginRequest.email());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("User or Password invalid!");
        }

        var now = Instant.now();
        var accessTokenExpiresIn = 300L;
        var refreshTokenExpiresIn = 604800L;

        var scopes = user.get().getRoles()
                .stream()
                .map(RoleEntity::getRoleName)
                .collect(Collectors.joining(" "));

        var accessTokenClaims = JwtClaimsSet.builder()
                .issuer("secret-santa")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpiresIn))
                .claim("scope", scopes)
                .build();

        var accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaims)).getTokenValue();

        var refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("secret-santa")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(refreshTokenExpiresIn))
                .build();

        var refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaims)).getTokenValue();


        LoginResponse response = new LoginResponse(accessToken, refreshToken, user.get().getUserId());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/api/token/refresh")
    public ResponseEntity<LoginResponse> refreshAccessToken(@RequestBody TokenRefreshRequestDto request) {

        var refreshToken = request.refreshToken();
        var now = Instant.now();
        var accessTokenExpiresIn = 300L;

        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(refreshToken);
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh Token inválid or expired!");
        }

        var user = userRepository.findById(UUID.fromString(jwt.getSubject()))
                .orElseThrow(() -> new RuntimeException("User not found!"));

        var scopes = user.getRoles()
                .stream()
                .map(RoleEntity::getRoleName)
                .map(role -> "SCOPE_" + role)
                .collect(Collectors.joining(" "));

        var accessTokenClaims = JwtClaimsSet.builder()
                .issuer("secret-santa")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenExpiresIn))
                .claim("scope", scopes)
                .build();

        var accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessTokenClaims)).getTokenValue();

        LoginResponse response = new LoginResponse(accessToken, refreshToken, user.getUserId());

        return  ResponseEntity.ok(response);
    }
}
