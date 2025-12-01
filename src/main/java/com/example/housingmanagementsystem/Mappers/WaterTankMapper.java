package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.WaterTankCreationDTO;
import com.example.housingmanagementsystem.DTOs.WaterTankSuccessfulCreationDTO;
import com.example.housingmanagementsystem.Models.WaterTank;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WaterTankMapper {
    //Entity to DTO
    WaterTankSuccessfulCreationDTO toDTO(WaterTank waterTank);

    //DTO to Entity
    WaterTank toEntity(WaterTankCreationDTO tankCreationDTO);
}
