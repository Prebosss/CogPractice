package com.example.bankapi.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import com.example.bankapi.models.User;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;

    public JwtService(
        JwtEncoder jwtEncoder,
        @Value("${jwt.expiration-minutes}")
        long expirationMinutes
    ) {
        this.jwtEncoder = jwtEncoder;
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("spring-bank-api")
            .issuedAt(now)
            .expiresAt(
                now.plus(
                    expirationMinutes,
                    ChronoUnit.MINUTES
                )
            )
            .subject(user.getId())
            .claim("username", user.getUsername())
            .build();

        JwsHeader header = JwsHeader
            .with(MacAlgorithm.HS256)
            .type("JWT")
            .build();

        JwtEncoderParameters parameters =
            JwtEncoderParameters.from(header, claims);

        return jwtEncoder
            .encode(parameters)
            .getTokenValue();
    }
}