package com.example.housingmanagementsystem.DTOs;

import com.example.housingmanagementsystem.Models.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenRotationResponseDTO {

    private RefreshToken refreshToken;
    private String rawRefreshToken;
}
