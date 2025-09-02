package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class UserRegistrationDTO {

    @NotBlank(message = "ID number must be provided")
    @Pattern(regexp = "\\d+")
    private String idNumber;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    private String emailAddress;
    private String userName;

    @Pattern(regexp = "\\+?\\d{10,15}" ,message = "Phone number must contain 10-15 digits")
    @NotBlank(message = "Phone number must be provided")
    private String phoneNumber;

    @NotNull(message = "Role must be specified")
    private Role role;
}
