package com.example.housingmanagementsystem.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
    @Email(message = "Invalid email format")
    private String emailAddress;

    @NotBlank(message = "Password must be provided")
    private String password;
}
