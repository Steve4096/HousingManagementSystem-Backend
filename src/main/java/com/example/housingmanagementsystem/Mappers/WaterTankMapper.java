package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.WaterTankCreationDTO;
import com.example.housingmanagementsystem.DTOs.WaterTankResponseDTO;
import com.example.housingmanagementsystem.DTOs.WaterTankSuccessfulCreationDTO;
import com.example.housingmanagementsystem.Models.WaterTank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WaterTankMapper {
    //Entity to DTO(on creation)
    WaterTankSuccessfulCreationDTO toDTO(WaterTank waterTank);

    //Entity to DTO(for viewing/display on the frontend)
    @Mapping(source = "waterVolume", target = "volume")
    WaterTankResponseDTO toDisplayDTO(WaterTank waterTank);

    //DTO to Entity
    WaterTank toEntity(WaterTankCreationDTO tankCreationDTO);
}
