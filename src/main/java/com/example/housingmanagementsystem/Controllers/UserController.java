package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.TenantRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserResponseDTO;
import com.example.housingmanagementsystem.DTOs.UserUpdateDTO;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor //Generates a constructor for all final fields and fields annotated with @NonNull
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerUser")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO){
       // return ResponseEntity.ok().body(userService.registerUser(registrationDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(registrationDTO));
    }

    //@PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> registerTenant(@Valid @RequestBody TenantRegistrationDTO registrationDTO){
        //return ResponseEntity.ok().body(userService.registerTenant(registrationDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerTenant(registrationDTO));
    }

    //@PreAuthorize("hasAnyRole('ADMIN','LANDLORD','USER')")
    @PreAuthorize("#id==authentication.principal.id or hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUserDetails(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO){
        return ResponseEntity.ok(userService.updateUserDetails(id, userUpdateDTO));
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> fetchAll(){
        return ResponseEntity.ok().body(userService.fetchAllUsers());
    }

    // @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
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
