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
    @Mapping(source = "payment.occupancy.user.fullName", target = "tenantName")
    @Mapping(source = "payment.occupancy.property.unitNumber", target = "unitNumber")
    @Mapping(source = "dateTimeOfTransaction", target = "timeOfTransaction")
    @Mapping(source = "transactionStatus", target = "status")
    PaymentResponseDTO toDTO(Payment payment);
}
