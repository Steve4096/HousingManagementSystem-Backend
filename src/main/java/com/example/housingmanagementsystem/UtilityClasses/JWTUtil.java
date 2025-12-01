package com.example.housingmanagementsystem.UtilityClasses;

import com.example.housingmanagementsystem.Configs.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Getter
public class JWTUtil {

    private final JWTProperties jwtProperties;
    private final SecretKey secretKey;

    public JWTUtil(JWTProperties jwtProperties){
        this.jwtProperties=jwtProperties;

        // Convert your secret string into a proper SecretKey for HS256
        this.secretKey= Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    //Central method that creates JWTs
    private String createToken(Map<String,Object> claims,String subject,long expiration){
        return Jwts.builder() //Builds the token step by step
                .setClaims(claims) //Accepts extra information such as roles
                .setSubject(subject) //Sets the username/who the token belongs to
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(secretKey,SignatureAlgorithm.HS256) //sign the token using HMAC-SHA256 and your secret key. Signing means anyone who changes the token later will fail verification.
                .compact(); //Converts the token generated into a string
    }

    //Generate an access token with custom claims e.g roles
    //Included roles so that the frontend can also know user roles and know which window to display
    public String generateAccessToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();

        //Add roles as a claim
        claims.put("roles",userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return createToken(claims,userDetails.getUsername(), jwtProperties.getAccessTokenExpiration());
    }

    //Generate a refresh token(usually no roles needed)
    public String generateRefreshToken(UserDetails userDetails) {
        return createToken(Collections.emptyMap(), userDetails.getUsername(), jwtProperties.getRefreshTokenExpiration());
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
        return Jwts.parserBuilder() //Gets a parser that understands my tokens
                .setSigningKey(secretKey) //Tells the parser which key to use in verifying the signature
                .build()
                .parseClaimsJws(token) //Verifies token integrity and signature
                .getBody(); //Used together with getSubject to get who the token belongs to
    }
}
