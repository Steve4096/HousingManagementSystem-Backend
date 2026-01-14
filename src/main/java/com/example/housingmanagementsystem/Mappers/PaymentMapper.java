package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.MakePaymentDTO;
import com.example.housingmanagementsystem.DTOs.PaymentResponseDTO;
import com.example.housingmanagementsystem.Models.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    //DTO -> Entity
    Payment toEntity(MakePaymentDTO makePaymentDTO);

    //Entity -> DTO
    @Mapping(source = "occupancy.user.fullName", target = "tenantName")
    @Mapping(source = "occupancy.property.unitNumber", target = "unitNumber")
    PaymentResponseDTO toDTO(Payment payment);
}
