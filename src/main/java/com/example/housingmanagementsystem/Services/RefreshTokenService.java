package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.Configs.JWTProperties;
import com.example.housingmanagementsystem.DTOs.RefreshTokenRotationResponseDTO;
import com.example.housingmanagementsystem.Models.RefreshToken;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.RefreshTokenRepository;
import com.example.housingmanagementsystem.Repositories.UserRepository;
import com.example.housingmanagementsystem.UtilityClasses.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final JWTProperties jwtProperties;
    private final PasswordEncoder tokenEncoder;

//    public RefreshToken createRefreshToken(User user,String refreshTokenString){
//        RefreshToken refreshToken=RefreshToken.builder()
//                .user(user)
//                //.token(UUID.randomUUID().toString()) //random unique string
//                //.token(refreshTokenString)
//                .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpiration()))
//                .createdAt(LocalDateTime.now())
//                .isRevoked(false)
//                .build();
//
//        return refreshTokenRepository.save(refreshToken);
//    }

    public RefreshToken createRefreshToken(
            User user,
            String refreshTokenString){

        String hashedToken =
                tokenEncoder.encode(refreshTokenString);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(hashedToken)
                .expiryDate(
                        Instant.now().plusMillis(
                                jwtProperties.getRefreshTokenExpiration()
                        )
                )
                .createdAt(LocalDateTime.now())
                .isRevoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }


//    public RefreshToken validateRefreshToken(String token){
//        RefreshToken refreshToken=refreshTokenRepository.findByToken(token)
//                .orElseThrow(()->new RuntimeException("Invalid refresh token"));
//
//        if(refreshToken.isRevoked()){
//            throw new RuntimeException("Refresh token has been revoked");
//        }
//
//        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
//            refreshToken.setRevoked(true);
//            refreshTokenRepository.save(refreshToken);
//            throw new RuntimeException("Refresh token expired.");
//        }
//        return refreshToken;
//    }

    public RefreshToken validateRefreshToken(String rawToken){

        // Validate JWT structure/signature first
        if(jwtUtil.isTokenExpired(rawToken)){
            throw new RuntimeException("Refresh token expired");
        }

        String username =
                jwtUtil.extractUsername(rawToken);

        User user =
                userRepository
                        .findByEmailAddress(username)
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        List<RefreshToken> storedTokens =
                refreshTokenRepository.findAllByUser(user);

        RefreshToken matchedToken = null;

        for(RefreshToken storedToken : storedTokens){

            boolean matches =
                    tokenEncoder.matches(
                            rawToken,
                            storedToken.getTokenHash()
                    );

            if(matches){
                matchedToken = storedToken;
                break;
            }
        }

        if(!jwtUtil.extractTokenType(rawToken).equals("refresh")){
            throw new RuntimeException("Invalid token type");
        }

        if(matchedToken == null){
            throw new RuntimeException("Invalid refresh token");
        }

        if(matchedToken.isRevoked()){
            throw new RuntimeException("Refresh token revoked");
        }

        if(matchedToken.getExpiryDate().isBefore(Instant.now())){

            matchedToken.setRevoked(true);
            refreshTokenRepository.save(matchedToken);

            throw new RuntimeException("Refresh token expired");
        }

        return matchedToken;
    }

    //Revoke an old refresh token,generate a new one and store the new one
//    public RefreshToken rotateRefreshToken(RefreshToken oldRefreshToken){
//        //revoke old refresh token
//        oldRefreshToken.setRevoked(true);
//        refreshTokenRepository.save(oldRefreshToken);
//
//        //revokeTokensByUser(oldRefreshToken.getUser());
//
//        User user=oldRefreshToken.getUser();
//
//        // create Spring Security UserDetails object
//        UserDetails userDetails=new org.springframework.security.core.userdetails.User(
//                user.getEmailAddress(),
//                user.getPasswordHash(),
//                List.of()
//        );
//
//        String newRefreshToken= jwtUtil.generateRefreshToken(userDetails);
//
//        return createRefreshToken(oldRefreshToken.getUser(),newRefreshToken);
//    }

    public RefreshTokenRotationResponseDTO rotateRefreshToken(
            RefreshToken oldRefreshToken){

        // revoke old refresh token
        oldRefreshToken.setRevoked(true);
        refreshTokenRepository.save(oldRefreshToken);

        User user = oldRefreshToken.getUser();

        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        user.getEmailAddress(),
                        user.getPasswordHash(),
                        List.of()
                );

        // generate NEW RAW refresh token
        String newRawRefreshToken =
                jwtUtil.generateRefreshToken(userDetails);

        // save HASHED version
        RefreshToken savedToken =
                createRefreshToken(user, newRawRefreshToken);

        // return BOTH
        return new RefreshTokenRotationResponseDTO(
                savedToken,
                newRawRefreshToken
        );
    }

    //Revoke all tokens for a user(On logout)
    public void revokeTokensByUser(User user){
        List<RefreshToken> refreshTokens=refreshTokenRepository.findAllByUser(user);

        refreshTokens.forEach(refreshToken -> refreshToken.setRevoked(true));
        refreshTokenRepository.saveAll(refreshTokens);
    }
}
