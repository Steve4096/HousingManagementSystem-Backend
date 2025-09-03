package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserResponseDTO;
import com.example.housingmanagementsystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

   // @PostMapping("/registerUser")

    @GetMapping("/fetchAllUsers")
    public ResponseEntity<List<UserResponseDTO>> fetchAllUsers(){
        List<UserResponseDTO> allUsers=userService.fetchAllUsers()
                .stream()
                .map(user -> new UserRegistrationDTO(user.getIdNumber(), user.getFullName(), user.getEmailAddress(), user.getPhoneNumber(),user.getRole()))
                .toList();

        return ResponseEntity.ok().body(allUsers);
    }


}
