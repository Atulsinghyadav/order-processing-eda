package com.orderprocessingeda.authservice.jwt;

import com.orderprocessingeda.authservice.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private  JwtProperties jwtProperties;
    public JwtService(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis()
                                + jwtProperties.getExpiration()
                        )
                )
                .signWith(
                        getSignKey(),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    private Key getSignKey(){

        byte[] keyBytes = jwtProperties.getSecretKey()
                .getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token){

        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token){

        return extractAllClaims(token)
                .getSubject();
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token)
                .getExpiration();
    }

    public boolean isTokenExpired(String token){

        return extractExpiration(token)
                .before(new Date());
    }

    public boolean isTokenValid(String token, String username){

        String extractedUsername = extractUsername(token);

        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

}
