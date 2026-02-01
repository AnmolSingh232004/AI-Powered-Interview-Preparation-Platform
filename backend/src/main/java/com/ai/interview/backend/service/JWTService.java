package com.ai.interview.backend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import com.ai.interview.backend.entity.User;

import java.util.Date;

@Service
public class JWTService {

    private static final String SECRET_KEY = "pretty_long_key_that_should_be_really_long";

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail()) // identifier is email for a user
                .claim("role", user.getRole()) // role of user i.e admin or user
                .setIssuedAt(new Date()) // current time exactly
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour in milliseconds
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256) // singing token
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
