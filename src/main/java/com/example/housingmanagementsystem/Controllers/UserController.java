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

    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

   // @PostMapping("/registerUser")

    @GetMapping("/fetchAllUsers")
    public ResponseEntity<List<UserResponseDTO>> fetchAllUsers(){
        return ResponseEntity.ok().body(userService.fetchAllUsers());
    }


}
