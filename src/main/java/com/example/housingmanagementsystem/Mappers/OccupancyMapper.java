package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.OccupancyResponseDTO;
import com.example.housingmanagementsystem.Models.Occupancy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OccupancyMapper {

    //Entity -> DTO
    @Mapping(source = "user.fullName",target = "fullName")
    @Mapping(source = "property.unitNumber",target = "unitNumber")
    OccupancyResponseDTO toDTO(Occupancy occupancy);

}
