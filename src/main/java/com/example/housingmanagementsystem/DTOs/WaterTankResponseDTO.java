package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.WaterLevelStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WaterTankResponseDTO {
    private Long id;
    private String tankName;
    private int capacity;
    private int volume;
    private WaterLevelStatus status;
}
