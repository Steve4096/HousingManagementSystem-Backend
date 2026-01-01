package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.ComplaintCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintResponseDTO {
    private Long id;
    private String tenantName;
    private ComplaintCategory category;
    private String description;
}
