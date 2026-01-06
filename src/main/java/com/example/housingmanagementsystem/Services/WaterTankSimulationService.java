package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.WaterTankResponseDTO;
import com.example.housingmanagementsystem.Services.WaterTankService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WaterTankSimulationService {

    private final WaterTankService waterTankService;
    private final Random random = new Random();

    // ----------------------
    // Runs every 5 seconds
    // ----------------------
    @Scheduled(fixedRate = 5000)
    public void simulateWaterFlow() {
        // Fetch all tanks as DTOs
        List<WaterTankResponseDTO> tanks = waterTankService.getAllTanks();

        for (WaterTankResponseDTO tankDTO : tanks) {
            // Randomly decide delta (-20 to +30 liters)
            int delta = random.nextInt(51) - 20;

            // Adjust water level and get updated DTO
            WaterTankResponseDTO updatedTank = waterTankService.adjustWaterVolume(tankDTO.getId(), delta);

            // Log status
            System.out.println("Tank: " + updatedTank.getTankName() +
                    " | Volume: " + updatedTank.getVolume() +
                    "/" + updatedTank.getCapacity() +
                    " | Status: " + updatedTank.getStatus());
        }
    }
}
