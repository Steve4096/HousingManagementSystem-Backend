package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.ComplaintCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintFillingDTO {
    @NotNull(message = "You must select the category to which your complaint belongs to from the options provided")
    private ComplaintCategory category;

    @NotBlank(message = "You must provide a brief description of the complaint")
    private String description;
}
