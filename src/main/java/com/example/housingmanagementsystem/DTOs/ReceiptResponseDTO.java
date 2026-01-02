package com.example.housingmanagementsystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptResponseDTO {
    private String receiptNumber;
    private BigDecimal amount;
    private String tenantName;
    private String unitNumber;
    private String createdBy;
    private LocalDateTime createdAt;
}
