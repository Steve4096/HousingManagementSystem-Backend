package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.UtilityClasses.ValidNoticePeriod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeFillingDTO {

    @NotNull(message = "The date of intended leave must be provided")
    @ValidNoticePeriod
    private LocalDateTime dateIntendToLeave;

    @NotNull
    private Long occupancyId;
}
