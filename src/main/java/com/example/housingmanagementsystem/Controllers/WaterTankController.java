package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.Services.WaterTankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/tank")
@RequiredArgsConstructor
public class WaterTankController {

    private final WaterTankService waterTankService;
}
