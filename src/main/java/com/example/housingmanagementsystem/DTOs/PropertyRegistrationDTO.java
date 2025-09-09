package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.Propertytype;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PropertyRegistrationDTO {
    @NotBlank(message = "A unit number must be provided")
    private String unitNumber;

    @NotNull(message = "Property type must be specified")
    private Propertytype propertytype;

    @NotNull(message = "Rent amount must be provided")
    @DecimalMin(value = "8500", message = "The minimum rent amount is 8500")
    private BigDecimal rentAmount;
}
