package com.example.housingmanagementsystem.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WaterTankCreationDTO {
    @NotBlank(message = "A name must be provided")
    private String tankName;

    @NotNull(message = "The capacity should be specified")
    private int capacity;
}
