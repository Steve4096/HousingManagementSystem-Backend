package com.example.housingmanagementsystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OccupancyResponseDTO {
    private String fullName;
    private String unitNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
