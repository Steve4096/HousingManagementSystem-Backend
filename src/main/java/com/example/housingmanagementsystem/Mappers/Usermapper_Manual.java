package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserResponseDTO;
import com.example.housingmanagementsystem.Models.User;

public class Usermapper_Manual {

    public static User toEntity(UserRegistrationDTO userRegistrationDTO){
        return User.builder()
                .idNumber(userRegistrationDTO.getIdNumber())
                .fullName(userRegistrationDTO.getFullName())
                .emailAddress(userRegistrationDTO.getEmailAddress())
                //.userName(userRegistrationDTO.getUserName())
                .phoneNumber(userRegistrationDTO.getPhoneNumber())
                .role(userRegistrationDTO.getRole())
                .build();
    }

    public static UserResponseDTO toDto(User userEntity){
        return UserResponseDTO.builder()
                .idNumber(userEntity.getIdNumber())
                .fullName(userEntity.getFullName())
                .emailAddress(userEntity.getEmailAddress())
                .phoneNumber(userEntity.getPhoneNumber())
                .role(userEntity.getRole())
                .build();

    }
}
