package com.github.fekom.secret_santa.infra.security;

import com.github.fekom.secret_santa.dtos.LoginRequest;
import com.github.fekom.secret_santa.dtos.LoginResposne;
import com.github.fekom.secret_santa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class TokenController {


    @Autowired
     private  JwtEncoder jwtEncoder;
    @Autowired
     private  UserRepository userRepository;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    public  TokenController() {

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResposne> login (@RequestBody LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.email());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("User or Password invalid!");
        }

        var now = Instant.now();
        var expiresIn = 300L;


        var claims = JwtClaimsSet.builder()
                .issuer("secret-santa")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResposne(jwtValue, expiresIn));
    }
}
