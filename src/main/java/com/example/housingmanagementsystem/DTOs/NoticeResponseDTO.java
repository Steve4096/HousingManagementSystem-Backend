package com.example.housingmanagementsystem.DTOs;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeResponseDTO {
    private String tenantName;
    private String unitNumber;
    private LocalDateTime createdAt;
    private LocalDateTime dateIntendToLeave;
}
