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
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;
    private final JWTProperties jwtProperties;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JWTUtil jwtUtil,JWTProperties jwtProperties){
        this.refreshTokenRepository=refreshTokenRepository;
        this.jwtUtil=jwtUtil;
        this.jwtProperties=jwtProperties;
    }

//    public RefreshToken createRefreshToken(User user){
//        RefreshToken refreshToken=RefreshToken.builder()
//                .user(user)
//                .token(UUID.randomUUID().toString()) //random unique string
//                .expiryDate(new Date(jwtProperties.getRefreshTokenExpiration()) //7 days
//
//
//        return refreshTokenRepository.save(refreshToken);
//    }

    public boolean validateRefreshToken(Long token){
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
