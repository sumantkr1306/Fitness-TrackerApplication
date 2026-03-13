package com.project.fitness.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.List;

@Component

public class JwtUtils {

    private String jwtsecret = "YS1zdHJpbmctc2VjcmV0LWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc=";
    private int JwtExpirationMs = 172800000;

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(jwt);
            return true;
        } catch (Exception e) {
            System.err.println("JWT Validation failed: " + e.getMessage());
            return false;
        }
    }

    public String generateToken(String userid, String role) {
        return Jwts.builder()
                .subject(userid)
                .claim("role", List.of(role))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JwtExpirationMs))
                .signWith(key()).compact();
    }


    public SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtsecret));
    }

    public String getUserIdFromToken(String jwt) {
        return getAllClaims(jwt).getSubject();
    }

    public Claims getAllClaims(String jwt) {
        return Jwts.parser().verifyWith(key())
                .build().parseSignedClaims(jwt).getPayload();
    }
}