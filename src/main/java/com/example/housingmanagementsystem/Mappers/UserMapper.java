package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.TenantRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserResponseDTO;
import com.example.housingmanagementsystem.DTOs.UserUpdateDTO;
import com.example.housingmanagementsystem.Models.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    //Entity -> DTO
    // Explicitly map ID from User -> UserResponseDTO
   // @Mapping(source = "id", target = "id")
    UserResponseDTO toDTO(User user);

    //DTO -> Entity
    User toEntity(UserRegistrationDTO registrationDTO);

    //DTO -> Entity
    //For tenant registration
    User toEntity(TenantRegistrationDTO tenantRegistrationDTO);

    //Updating an entity(Partial updates)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserfromDTO(UserUpdateDTO updateDTO, @MappingTarget User user);
}
