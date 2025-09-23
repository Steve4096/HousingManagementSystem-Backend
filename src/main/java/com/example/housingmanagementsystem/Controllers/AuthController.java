package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.LoginDTO;
import com.example.housingmanagementsystem.Services.RefreshTokenService;
import com.example.housingmanagementsystem.UtilityClasses.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,JWTUtil jwtUtil,UserDetailsService userDetailsService,RefreshTokenService refreshTokenService){
        this.authenticationManager=authenticationManager;
        this.jwtUtil=jwtUtil;
        this.userDetailsService=userDetailsService;
        this.refreshTokenService=refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request){
        // Authenticate user (this will call UserDetailsService under the hood)
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmailAddress(),request.getPassword())
        );

        // Extract user details from authentication
        var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        //Generate tokens
        String accessToken= jwtUtil.generateAccessToken(userDetails);
        String refreshToken=jwtUtil.generateRefreshToken(userDetails);

        return ResponseEntity.ok().body(
                Map.of("accessToken",accessToken,
                        "refreshToken",refreshToken)
        );
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
//        String requestRefreshToken = request.get("refreshToken");
//
//        if (requestRefreshToken == null) {
//            return ResponseEntity.badRequest().body("Refresh token is required");
//        }
//
//        try {
//            // Validate token in DB
//            refreshTokenService.validateRefreshToken(requestRefreshToken);
//
//            // Get the refresh token entry
//            RefreshToken storedToken = refreshTokenRepository.findByToken(requestRefreshToken)
//                    .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
//
//            User user = storedToken.getUser();
//
//            // Generate new tokens
//            var userDetails = userDetailsService.loadUserByUsername(user.getEmailAddress());
//            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
//
//            // Optionally, delete old refresh token and issue a new one
//            refreshTokenService.deleteByUser(user);
//            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
//
//            return ResponseEntity.ok(Map.of(
//                    "accessToken", newAccessToken,
//                    "refreshToken", newRefreshToken.getToken()
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Invalid or expired refresh token");
//        }
//    }

}
