package com.example.housingmanagementsystem.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String fullName;

    @Email(message = "Incorrect email address format")
    private String emailAddress;

    @Pattern(regexp = "\\+?\\d{10,15}" ,message = "Phone number must contain 10-15 digits")
    private String phoneNumber;
    private String userName;
}
