package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.PropertyType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectedPropertyDTO {
    private Long occupancyId;
    private String unitNumber;
    private PropertyType propertyType;
}
