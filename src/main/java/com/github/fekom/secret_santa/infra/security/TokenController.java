package com.github.fekom.secret_santa.infra.security;

import com.github.fekom.secret_santa.model.dto.LoginRequest;
import com.github.fekom.secret_santa.model.dto.LoginResposne;
import com.github.fekom.secret_santa.apiResponse.AddParticipantsResponse;
import com.github.fekom.secret_santa.entity.RoleEntity;
import com.github.fekom.secret_santa.repository.RoleRepository;
import com.github.fekom.secret_santa.repository.UserRepository;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Login", description = "Login Endpoint")
public class TokenController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private  JwtEncoder jwtEncoder;

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
                schema = @Schema(implementation = LoginResposne.class),
                mediaType = "application/json"
            )} 
        ),
        @ApiResponse(responseCode = "404", description = "invalid email or password",
            content = {@Content}
        ),
    })
    public ResponseEntity<LoginResposne> login (@RequestBody LoginRequest loginRequest) {

        var user = userRepository.findByEmail(loginRequest.email());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("User or Password invalid!");
        }

        var now = Instant.now();
        var expiresIn = 10800L;

        var scopes = user.get().getRoles()
                .stream()
                .map(RoleEntity::getRoleName)
                .collect(Collectors.joining(""));

        var claims = JwtClaimsSet.builder()
                .issuer("secret-santa")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        LoginResposne response = new LoginResposne(jwtValue, expiresIn, user.get().getUserId());

        return ResponseEntity.ok(response);
    }
}
