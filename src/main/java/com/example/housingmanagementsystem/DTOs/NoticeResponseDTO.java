package com.example.housingmanagementsystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NoticeResponseDTO {
    private Long id;
    private String tenantName;
    private String unitNumber;
    private LocalDateTime createdAt;
    private LocalDateTime dateIntendToLeave;
}
