package com.example.housingmanagementsystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OccupancyCreationDTO {
    private Long userId;
    private Long propertyId;
}
