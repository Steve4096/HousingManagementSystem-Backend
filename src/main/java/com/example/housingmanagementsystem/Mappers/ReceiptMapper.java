package com.example.housingmanagementsystem.Mappers;

import com.example.housingmanagementsystem.DTOs.ReceiptResponseDTO;
import com.example.housingmanagementsystem.Models.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    //Entity -> DTO
    @Mapping(source = "payment.amount", target = "amount")
    @Mapping(source = "payment.occupancy.user.fullName", target = "tenantName")
    @Mapping(source = "payment.occupancy.property.unitNumber", target = "unitNumber")
    @Mapping(source = "payment.paymentFor", target = "paymentFor")
    @Mapping(source = "payment.monthPaidFor", target = "monthPaidFor")
    ReceiptResponseDTO toDTO(Receipt receipt);
}
