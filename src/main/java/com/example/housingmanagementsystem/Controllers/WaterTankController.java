package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.WaterTankCreationDTO;
import com.example.housingmanagementsystem.DTOs.WaterTankResponseDTO;
import com.example.housingmanagementsystem.DTOs.WaterTankSuccessfulCreationDTO;
import com.example.housingmanagementsystem.Services.WaterTankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/water-tanks")
@RequiredArgsConstructor
public class WaterTankController {

    private final WaterTankService waterTankService;

   //@PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("/create")
    public ResponseEntity<WaterTankSuccessfulCreationDTO> saveWaterTankDetails(@Valid @RequestBody WaterTankCreationDTO waterTankCreationDTO){
        return ResponseEntity.ok(waterTankService.createWaterTank(waterTankCreationDTO));
    }

    //@PreAuthorize("hasRole('LANDLORD')")
    @GetMapping
    public ResponseEntity<List<WaterTankResponseDTO>> fetchAllTankDetails(){
        return ResponseEntity.ok(waterTankService.getAllTanks());
    }

    //@PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("/{id}/drain")
    public ResponseEntity<WaterTankResponseDTO> drainWaterTankCompletely(@PathVariable Long id){
        return ResponseEntity.ok(waterTankService.drainTank(id));
    }

    //@PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("/{id}/fill")
    public ResponseEntity<WaterTankResponseDTO> fillWaterTankCompletely(@PathVariable Long id){
        return ResponseEntity.ok(waterTankService.fillTank(id));
    }

    //@PreAuthorize("hasRole('LANDLORD')")
    @PostMapping("/{id}/adjust")
    public ResponseEntity<WaterTankResponseDTO> adjustWaterLevels(@PathVariable Long id,@RequestParam int litres){
        return ResponseEntity.ok(waterTankService.adjustWaterVolume(id,litres));
    }
}
