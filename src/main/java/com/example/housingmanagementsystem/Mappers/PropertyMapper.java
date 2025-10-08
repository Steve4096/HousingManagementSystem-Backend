package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.PropertyRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.PropertyResponseDTO;
import com.example.housingmanagementsystem.Models.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring" ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PropertyMapper {

    //Entity -> DTO
    //Explicitly map ID from Property to PropertyResponseDTO
    //@Mapping(source = "id", target = "id")
    PropertyResponseDTO toDTO(Property property);

    //DTO -> Entity
    Property toEntity(PropertyRegistrationDTO propertyRegistrationDTO);
}
