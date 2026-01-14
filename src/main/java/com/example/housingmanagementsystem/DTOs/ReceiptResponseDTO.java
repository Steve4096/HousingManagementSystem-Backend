package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.PaymentFor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptResponseDTO {
    private Long id;
    private String receiptNumber;
    private BigDecimal amount;
    private String tenantName;
    private String unitNumber;
    private String createdBy;
    private LocalDateTime createdAt;
    private BigDecimal balance;
    private PaymentFor paymentFor;
    private LocalDate monthPaidFor;
}
