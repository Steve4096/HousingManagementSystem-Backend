package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.AuthResponseDTO;
import com.example.housingmanagementsystem.DTOs.LoginDTO;
import com.example.housingmanagementsystem.DTOs.RefreshTokenRotationResponseDTO;
import com.example.housingmanagementsystem.DTOs.TokenRefreshRequestDTO;
import com.example.housingmanagementsystem.Models.RefreshToken;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.UserRepository;
import com.example.housingmanagementsystem.Services.RefreshTokenService;
import com.example.housingmanagementsystem.Services.UserService;
import com.example.housingmanagementsystem.UtilityClasses.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDTO request){
//        // Authenticate user (this will call UserDetailsService under the hood)
//        Authentication authentication= authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmailAddress(),request.getPassword())
//        );
//
//        // Extract user details from authentication
//        var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
//        //var user=userRepository.findByEmailAddress(userDetails.getUsername());
//        var user=userService.findUSerByEmail(request.getEmailAddress());
//
//        //Generate tokens
//        String accessToken= jwtUtil.generateAccessToken(userDetails);
//        String refreshToken=jwtUtil.generateRefreshToken(userDetails);
//
//        //Save refresh token in DB
//        refreshTokenService.createRefreshToken(user,refreshToken);
//
//        return ResponseEntity.ok(new AuthResponseDTO(accessToken,refreshToken));
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDTO request){

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmailAddress(),
                                request.getPassword()
                        )
                );

        var userDetails =
                (org.springframework.security.core.userdetails.User)
                        authentication.getPrincipal();

        var user =
                userService.findUSerByEmail(
                        request.getEmailAddress()
                );

        String accessToken =
                jwtUtil.generateAccessToken(userDetails);

        String refreshToken =
                jwtUtil.generateRefreshToken(userDetails);

        refreshTokenService.createRefreshToken(
                user,
                refreshToken
        );

        // CREATE COOKIE
        ResponseCookie refreshCookie =
                ResponseCookie.from(
                                "refreshToken",
                                refreshToken
                        )
                        .httpOnly(true)
                        .secure(true)
                        .path("/api/auth")
                        .maxAge(7 * 24 * 60 * 60)
                        .sameSite("Strict")
                        .build();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        refreshCookie.toString()
                )
                .body(
                        Map.of(
                                "accessToken",
                                accessToken
                        )
                );
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(
//            @RequestBody TokenRefreshRequestDTO request){
//
//        try {
//
//            String requestRefreshToken =
//                    request.getRefreshToken();
//
//            // validate token in DB
//            RefreshToken oldToken =
//                    refreshTokenService
//                            .validateRefreshToken(requestRefreshToken);
//
//            User user = oldToken.getUser();
//
//            UserDetails userDetails =
//                    userDetailsService.loadUserByUsername(
//                            user.getEmailAddress()
//                    );
//
//            // generate new access token
//            String newAccessToken =
//                    jwtUtil.generateAccessToken(userDetails);
//
//            // rotate refresh token
//            RefreshToken newRefreshToken =
//                    refreshTokenService.rotateRefreshToken(oldToken);
//
//            return ResponseEntity.ok(
//                    new AuthResponseDTO(
//                            newAccessToken,
//                            newRefreshToken.getToken()
//                    )
//            );
//
//        } catch (Exception e){
//
//            return ResponseEntity
//                    .status(401)
//                    .body(Map.of(
//                            "error",
//                            e.getMessage()
//                    ));
//        }
//    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request){

        try {

            String refreshToken = null;

            // READ COOKIE
            if(request.getCookies() != null){

                for(Cookie cookie : request.getCookies()){

                    if(cookie.getName().equals("refreshToken")){
                        refreshToken = cookie.getValue();
                    }
                }
            }

            if(refreshToken == null){
                return ResponseEntity
                        .status(401)
                        .body("Refresh token missing");
            }

            RefreshToken oldToken =
                    refreshTokenService
                            .validateRefreshToken(refreshToken);

            User user = oldToken.getUser();

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(
                            user.getEmailAddress()
                    );

            String newAccessToken =
                    jwtUtil.generateAccessToken(userDetails);

//            RefreshToken newRefreshToken =
//                    refreshTokenService
//                            .rotateRefreshToken(oldToken);

            // NEW COOKIE
//            ResponseCookie newCookie =
//                    ResponseCookie.from(
//                                    "refreshToken",
//                                    newRefreshToken.getToken()
//                            )
//                            .httpOnly(true)
//                            .secure(true)
//                            .path("/api/auth")
//                            .maxAge(7 * 24 * 60 * 60)
//                            .sameSite("Strict")
//                            .build();

            RefreshTokenRotationResponseDTO tokenRotationResponseDTO=refreshTokenService.rotateRefreshToken(oldToken);

            ResponseCookie newCookie=ResponseCookie.from(
                    "refreshToken", tokenRotationResponseDTO.getRawRefreshToken()
            )
                    .httpOnly(true)
                    .secure(true)
                    .path("/api/auth")
                    .maxAge(7*24*60*60)
                    .sameSite("Strict")
                    .build();


            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.SET_COOKIE,
                            newCookie.toString()
                    )
                    .body(
                            Map.of(
                                    "accessToken",
                                    newAccessToken
                            )
                    );

        } catch (Exception e){

            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "error",
                            e.getMessage()
                    ));
        }
    }

    //When logging out,reset/delete all refresh tokens
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(Authentication authentication){
//        String username=authentication.getName();
//        var user=userService.findUSerByEmail(username);
//        refreshTokenService.revokeTokensByUser(user);
//        // clear current request security context
//        SecurityContextHolder.clearContext();
//        return ResponseEntity.ok(Map.of("message","Logged out successfully"));
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            Authentication authentication){

        String username = authentication.getName();

        var user =
                userService.findUSerByEmail(username);

        refreshTokenService.revokeTokensByUser(user);

        SecurityContextHolder.clearContext();

        // DELETE COOKIE
        ResponseCookie deleteCookie =
                ResponseCookie.from(
                                "refreshToken",
                                ""
                        )
                        .httpOnly(true)
                        .secure(true)
                        .path("/api/auth")
                        .maxAge(0)
                        .sameSite("Strict")
                        .build();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        deleteCookie.toString()
                )
                .body(
                        Map.of(
                                "message",
                                "Logged out successfully"
                        )
                );
    }
}
