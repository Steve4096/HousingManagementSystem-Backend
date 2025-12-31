package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.NoticeFillingDTO;
import com.example.housingmanagementsystem.DTOs.NoticeResponseDTO;
import com.example.housingmanagementsystem.Models.Notice;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NoticeMapper {

    //DTO -> Entity
    Notice toEntity(NoticeFillingDTO noticeFillingDTO);

    //Entity -> DTO
    @Mapping(source = "occupancy.user.fullName", target = "tenantName")
    @Mapping(source = "occupancy.property.unitNumber", target = "unitNumber")
    NoticeResponseDTO toDTO(Notice notice);

    //Updating a notice filed
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateExistingNotice(NoticeFillingDTO fillingDTO, @MappingTarget Notice notice);
}
