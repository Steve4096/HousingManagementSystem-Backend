package com.example.housingmanagementsystem.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class PropertyUpdateDTO {

    @DecimalMin(value = "8500",message = "The minimum rent amount is 8500")
    private BigDecimal rentAmount;
}
