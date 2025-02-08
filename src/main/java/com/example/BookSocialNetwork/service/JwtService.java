package com.example.BookSocialNetwork.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

public class JwtService {
    private final String secretKeyString;

    public JwtService() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("Hmac256");
        SecretKey secretKey = keyGen.generateKey();
        secretKeyString= Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    private SecretKey getKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails user){
        Map<String, Object> claims = new HashMap<>();

        var authorities = user.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority);
        return Jwts
                .builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 2*60*60*1000))
                .claim("authorities",authorities)
                .signWith(getKey())
                .compact();
    }


    public String extractUsername(String token) {
       return extractClaim(token,Claims::getSubject);
    }


    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public boolean validate(String token, UserDetails userDetails) {
        return !isExpired(token) && extractUsername(token).equals(userDetails.getUsername());
    }

    private boolean isExpired(String token){
        return extractExpirationDate(token).before(new Date());
    }

    private Date extractExpirationDate(String token){
        return extractClaim(token,Claims::getExpiration);
    }
}
