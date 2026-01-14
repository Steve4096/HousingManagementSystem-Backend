package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.PaymentFor;
import com.example.housingmanagementsystem.UtilityClasses.PaymentMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @NotNull(message = "You must select how you want to proceed with the payment")
    private PaymentMode paymentMode;

    @NotNull(message = "You must select what type of payment you are making")
    private PaymentFor paymentFor;

    private LocalDate monthPaidFor;
}
