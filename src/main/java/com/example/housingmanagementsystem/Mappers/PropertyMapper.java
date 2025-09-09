package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.PropertyRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.PropertyResponseDTO;
import com.example.housingmanagementsystem.DTOs.PropertyUpdateDTO;
import com.example.housingmanagementsystem.Models.Property;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    //DTO -> Entity
    Property toEntity(PropertyRegistrationDTO registrationDTO);

    //Entity -> DTO
    PropertyResponseDTO toDTO(Property property);

    //Updating an existing record
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePropertyEntity(PropertyUpdateDTO updateDTO, @MappingTarget Property property);
}
