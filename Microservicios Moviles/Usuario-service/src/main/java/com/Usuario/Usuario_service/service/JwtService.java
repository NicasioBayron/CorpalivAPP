package com.Usuario.Usuario_service.service;

import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service 
public class JwtService {

    // Clave de al menos 32 caracteres (256 bits) para evitar WeakKeyException
    private static final String SECRET = "claveUltraSecretaDeCorpaliv2026ParaElProyectoBackendJWT!";
    
    // Directamente SecretKey para no necesitar castear luego
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String email) {
        Date ahora = new Date();
        Date expiration = new Date(ahora.getTime() + 1000 * 60 * 60); // 1 hora
        return Jwts.builder()
                .subject(email)
                .issuedAt(ahora)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public String getEmailFromToken(String token) {
        if (token == null || token.isBlank()) return null;
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        try {
            return Jwts.parser()
                    .verifyWith(key) // Ya no necesita (SecretKey) key
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isValid(String token) {
        if (token == null || token.isBlank()) return false;
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
