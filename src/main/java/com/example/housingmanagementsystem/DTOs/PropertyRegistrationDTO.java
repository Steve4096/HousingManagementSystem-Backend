package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.PropertyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
public class PropertyRegistrationDTO {

    @NotBlank
    private String unitNumber;

    @NotNull
    private PropertyType propertyType;

    @NotNull
    @DecimalMin(value = "8500",message = "The minimum rent amount is 8500")
    private BigDecimal rentAmount;
}
