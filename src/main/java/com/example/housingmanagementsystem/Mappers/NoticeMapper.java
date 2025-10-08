package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.NoticeFillingDTO;
import com.example.housingmanagementsystem.DTOs.NoticeResponseDTO;
import com.example.housingmanagementsystem.Models.Notice;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface NoticeMapper {

    //DTO -> Entity
    Notice toEntity(NoticeFillingDTO noticeFillingDTO);

    //Entity -> DTO
    NoticeResponseDTO toDTO(Notice notice);

    //Updating a notice filed
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateExistingNotice(NoticeFillingDTO fillingDTO, @MappingTarget Notice notice);
}
