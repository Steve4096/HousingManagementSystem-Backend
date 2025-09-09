package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserResponseDTO;
import com.example.housingmanagementsystem.DTOs.UserUpdateDTO;
import com.example.housingmanagementsystem.Models.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    //Entity -> DTO
    UserResponseDTO toDTO(User user);

    //DTO -> Entity
    User toEntity(UserRegistrationDTO registrationDTO);

    //Updating an entity(Partial updates)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserfromDTO(UserUpdateDTO updateDTO, @MappingTarget User user);
}
