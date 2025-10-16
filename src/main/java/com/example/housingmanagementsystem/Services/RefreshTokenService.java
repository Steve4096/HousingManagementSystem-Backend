package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.Configs.JWTProperties;
import com.example.housingmanagementsystem.Models.RefreshToken;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.RefreshTokenRepository;
import com.example.housingmanagementsystem.UtilityClasses.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;
    private final JWTProperties jwtProperties;
    //private final UserRepository userRepository;


    public RefreshToken createRefreshToken(User user,String refreshTokenString){
        RefreshToken refreshToken=RefreshToken.builder()
                .user(user)
                //.token(UUID.randomUUID().toString()) //random unique string
                .token(refreshTokenString)
                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpiration()))
                .createdAt(LocalDateTime.now())
                .isRevoked(false)
                .build();
                //.expiryDate(new Date(jwtProperties.getRefreshTokenExpiration()) //7 days

        return refreshTokenRepository.save(refreshToken);
    }

//    public boolean validateRefreshToken(String token){
//        RefreshToken refreshToken=refreshTokenRepository.findByToken(token)
//                .orElseThrow(()->new NotFoundException("Invalid refresh token"));
//
//        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
//            refreshTokenRepository.delete(refreshToken);
//            throw new RuntimeException("Refresh token expired.Please log in again");
//        }
//        return true;
//    }

    public RefreshToken validateRefreshToken(String token){
        RefreshToken refreshToken=refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new RuntimeException("Invalid refresh token"));

        if(refreshToken.isRevoked()){
            throw new RuntimeException("Refresh token has been revoked");
        }

        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshToken.setRevoked(true);
            //refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired.");
        }
        return refreshToken;
    }

    //Revoke an old refresh token,generate a new one and store the new one
    public RefreshToken rotateRefreshToken(RefreshToken oldRefreshToken){
        //revoke old refresh token
        revokeTokensByUser(oldRefreshToken.getUser());

        //Generate new refresh token
        String newRefreshToken= jwtUtil.generateRefreshToken(
                new org.springframework.security.core.userdetails.User(
                        oldRefreshToken.getUser().getEmailAddress(),
                        oldRefreshToken.getUser().getPasswordHash(),
                        List.of() //contains user roles
                )
        );

        return createRefreshToken(oldRefreshToken.getUser(),newRefreshToken);
    }

    //Revoke all tokens for a user(On logout or rotation)
    public void revokeTokensByUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }

    public void deleteRefreshToken(RefreshToken refreshToken){
        refreshTokenRepository.delete(refreshToken);
    }
}
