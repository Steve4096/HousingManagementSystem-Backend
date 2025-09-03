package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String idNumber;
    private String fullName;
    private String emailAddress;
    private String userName;
    private String phoneNumber;
    private Role role;
}
