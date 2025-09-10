package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.ValidNoticePeriod;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class NoticeFillingDTO {

    @NotNull(message = "The date of intended leave must be provided")
    @ValidNoticePeriod
    private LocalDateTime dateIntendToLeave;
}
