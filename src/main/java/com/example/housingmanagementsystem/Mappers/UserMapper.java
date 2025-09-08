package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserResponseDTO;
import com.example.housingmanagementsystem.Models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE= Mappers.getMapper(UserMapper.class);

    //Entity -> DTO
    UserResponseDTO toDTO(User user);

    //DTO -> Entity
    User toEntity(UserRegistrationDTO registrationDTO);
}
