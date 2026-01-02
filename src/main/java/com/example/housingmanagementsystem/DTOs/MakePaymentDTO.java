package com.example.housingmanagementsystem.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MakePaymentDTO {

    @NotNull(message = "The amount must be provided")
    @DecimalMin(value = "850",message = "The minimum amount payable is 850")
    private BigDecimal amount;

    @NotNull(message = "You must specify which property you are making the payment for")
    private Long occupancyId;
}
