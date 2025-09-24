package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserResponseDTO;
import com.example.housingmanagementsystem.DTOs.UserUpdateDTO;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO){
       // return ResponseEntity.ok().body(userService.registerUser(registrationDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(registrationDTO));
    }

    @PostMapping("/tenant")
    public ResponseEntity<UserResponseDTO> registerTenant(@Valid @RequestBody UserRegistrationDTO registrationDTO){
        //return ResponseEntity.ok().body(userService.registerTenant(registrationDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerTenant(registrationDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUserDetails(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO){
        return ResponseEntity.ok(userService.updateUserDetails(id, userUpdateDTO));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> fetchAll(){
        return ResponseEntity.ok().body(userService.fetchAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        boolean deleted=userService.deleteUser(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }



}
