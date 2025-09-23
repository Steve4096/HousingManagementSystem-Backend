package com.example.housingmanagementsystem.UtilityClasses;

import com.example.housingmanagementsystem.Configs.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    private final JWTProperties jwtProperties;
    private final SecretKey secretKey;

    public JWTUtil(JWTProperties jwtProperties){
        this.jwtProperties=jwtProperties;

        // Convert your secret string into a proper SecretKey for HS256
        this.secretKey= Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private String createToken(Map<String,Object> claims,String subject,long expiration){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .compact();
    }

    //Generate an access token with custom claims e.g roles
    public String generateAccessToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();

        //Add roles as a claim
        claims.put("roles",userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return createToken(claims,userDetails.getUsername(), jwtProperties.getAccessTokenExpiration());
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject(); //subject=username/email
    }

    public boolean validateToken(String token,UserDetails userDetails){
        try {
            String username=extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false; //Invalid token or expired token
        }
    }

    public List<String> extractRoles(String token){
        Claims claims=extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
