package com.example.housingmanagementsystem.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantRegistrationDTO {

    @NotBlank(message = "ID number must be provided")
    @Pattern(regexp = "\\d+")
    private String idNumber;

    @NotBlank(message = "Full name must be provided")
    private String fullName;

    @Email(message = "Invalid email format")
    private String emailAddress;
    private String userName;

    @Pattern(regexp = "\\+?\\d{10,15}" ,message = "Phone number must contain 10-15 digits")
    @NotBlank(message = "Phone number must be provided")
    private String phoneNumber;

    @NotNull(message = "House number must be selected")
    private Long propertyId;
}
