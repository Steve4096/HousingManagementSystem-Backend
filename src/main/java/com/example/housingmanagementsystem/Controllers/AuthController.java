package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.AuthResponseDTO;
import com.example.housingmanagementsystem.DTOs.LoginDTO;
import com.example.housingmanagementsystem.DTOs.TokenRefreshRequestDTO;
import com.example.housingmanagementsystem.Models.RefreshToken;
import com.example.housingmanagementsystem.Services.RefreshTokenService;
import com.example.housingmanagementsystem.Services.UserService;
import com.example.housingmanagementsystem.UtilityClasses.JWTUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request){
        // Authenticate user (this will call UserDetailsService under the hood)
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmailAddress(),request.getPassword())
        );

        // Extract user details from authentication
        var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        var user=userService.findUSerByEmail(request.getEmailAddress());

        //Generate tokens
        String accessToken= jwtUtil.generateAccessToken(userDetails);
        String refreshToken=jwtUtil.generateRefreshToken(userDetails);

        //Save refresh token in DB
        refreshTokenService.createRefreshToken(user,refreshToken);

//        return ResponseEntity.ok().body(
//                Map.of("accessToken",accessToken,
//                        "refreshToken",refreshToken)
//     );

        return ResponseEntity.ok(new AuthResponseDTO(accessToken,refreshToken));
    }

//    public ResponseEntity<?> refreshToken(@RequestBody Map<String,String> request){
//        String requestRefreshToken=request.get("refresh token");
//        if(requestRefreshToken==null){
//            return ResponseEntity.badRequest().body("Refresh token is required");
//        }
//
//        try {
//            RefreshToken oldToken=refreshTokenService.validateRefreshToken(requestRefreshToken);
//
//            //Load user and generate new access token
//            var user=oldToken.getUser();
//            var userDetails=userDetailsService.loadUserByUsername(user.getEmailAddress());
//            String newAccessToken= jwtUtil.generateAccessToken(userDetails);
//
//            //Revoke old/used refresh token and issue new refresh token
//            RefreshToken newRefreshToken=refreshTokenService.rotateRefreshToken(oldToken);
//
////            return ResponseEntity.ok(Map.of(
////                    "accessToken",newAccessToken,
////                    "refreshToken",newRefreshToken.getToken()
////            ));
//
//            return ResponseEntity.ok(new AuthResponseDTO(newAccessToken,newRefreshToken.getToken()));
//
//        }catch (Exception e){
//            return ResponseEntity.status(401).body(Map.of("error",e.getMessage()));
//        }
//    }


/*public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequestDTO request){
    String requestRefreshToken=request.get("refresh token");
    if(requestRefreshToken==null){
        return ResponseEntity.badRequest().body("Refresh token is required");
    }

    try {
        RefreshToken oldToken=refreshTokenService.validateRefreshToken(requestRefreshToken);

        //Load user and generate new access token
        var user=oldToken.getUser();
        var userDetails=userDetailsService.loadUserByUsername(user.getEmailAddress());
        String newAccessToken= jwtUtil.generateAccessToken(userDetails);

        //Revoke old/used refresh token and issue new refresh token
        RefreshToken newRefreshToken=refreshTokenService.rotateRefreshToken(oldToken);

        return ResponseEntity.ok(new AuthResponseDTO(newAccessToken,newRefreshToken.getToken()));

    }catch (Exception e){
        return ResponseEntity.status(401).body(Map.of("error",e.getMessage()));
    }
}*/


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

    //When logging out,reset/delete all refresh tokens
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication){
        String username=authentication.getName();
        var user=userService.findUSerByEmail(username);
        refreshTokenService.revokeTokensByUser(user);
        return ResponseEntity.ok(Map.of("message","Logged out successfully"));
    }

}
