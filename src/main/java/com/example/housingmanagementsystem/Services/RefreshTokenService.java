package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Models.RefreshToken;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository=refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken=RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString()) //random unique string
                .expiryDate(Instant.now().plusSeconds(7*24*60*60)) //7 days
                .build();

        return refreshTokenRepository.save(refreshToken);
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
