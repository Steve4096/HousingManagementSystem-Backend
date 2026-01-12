package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.ComplaintFillingDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintResponseDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintUpdateDTO;
import com.example.housingmanagementsystem.Models.Complaint;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ComplaintMapper {
    //Entity -> DTO
    @Mapping(source = "complaintCategory", target = "category")
    @Mapping(source = "complaintDescription", target = "description")
    @Mapping(source = "user.fullName", target = "tenantName")
    ComplaintResponseDTO toDTO(Complaint complaint);

    //DTO -> Entity
    @Mapping(source = "category", target = "complaintCategory")
    @Mapping(source = "description", target = "complaintDescription")
    Complaint toEntity(ComplaintFillingDTO complaintFillingDTO);

    //Updating a complaint
    @Mapping(source = "category", target = "complaintCategory")
    @Mapping(source = "description", target = "complaintDescription")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateComplaint(ComplaintUpdateDTO updateDTO, @MappingTarget Complaint complaint);
}
