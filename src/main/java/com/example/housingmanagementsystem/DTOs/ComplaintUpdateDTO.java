package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.ComplaintCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintUpdateDTO {
    private ComplaintCategory category;
    private String description;
}
