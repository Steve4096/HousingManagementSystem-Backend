package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.OccupancyResponseDTO;
import com.example.housingmanagementsystem.Models.Occupancy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OccupancyMapper {

    //Entity -> DTO
    OccupancyResponseDTO toDTO(Occupancy occupancy);

}
