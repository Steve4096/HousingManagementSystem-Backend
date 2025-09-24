package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.Configs.JWTProperties;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Models.RefreshToken;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.RefreshTokenRepository;
import com.example.housingmanagementsystem.UtilityClasses.JWTUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JWTUtil jwtUtil){
        this.refreshTokenRepository=refreshTokenRepository;
        this.jwtUtil=jwtUtil;
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken=RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString()) //random unique string
                .expiryDate(Instant.now().plusSeconds(7*24*60*60)) //7 days
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public String createRefreshToken(UserDetails userDetails){
        String token= jwtUtil.generateRefreshToken(userDetails);

        RefreshToken refreshToken=new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserDetails(userDetails.getUsername());
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtUtil.getJwtProperties().getRefreshTokenExpiration()));

        refreshTokenRepository.save(token);

        return token;
    }

    public boolean validateRefreshToken(String token){
        RefreshToken refreshToken=refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new NotFoundException("Invalid refresh token"));

        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired.Please log in again");
        }
        return true;
    }

    //Revoke token
    public void revokeToken(RefreshToken refreshToken){

    }

    //Revoke all tokens for a user

    //Revoke an old refresh token,generate a new one and store the new one

    public void deleteByUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }
}
