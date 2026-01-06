package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.WaterTankCreationDTO;
import com.example.housingmanagementsystem.DTOs.WaterTankResponseDTO;
import com.example.housingmanagementsystem.DTOs.WaterTankSuccessfulCreationDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.WaterTankMapper;
import com.example.housingmanagementsystem.Models.WaterTank;
import com.example.housingmanagementsystem.Repositories.WaterTankRepository;
import com.example.housingmanagementsystem.UtilityClasses.WaterLevelStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaterTankService {

    private final WaterTankRepository waterTankRepository;
    private final WaterTankMapper waterTankMapper;


    // Create a new tank
    public WaterTankSuccessfulCreationDTO createWaterTank(WaterTankCreationDTO dto) {
        if (waterTankRepository.existsByTankName(dto.getTankName())) {
            throw new IllegalArgumentException("A tank with this name already exists");
        }

        WaterTank tank = waterTankMapper.toEntity(dto);
        tank.setWaterVolume(0);
        tank.setStatus(WaterLevelStatus.EMPTY);

        WaterTank saved = waterTankRepository.save(tank);
        return waterTankMapper.toDTO(saved);
    }


    // Adjust water by litres
    public WaterTankResponseDTO adjustWaterVolume(Long id, int litres) {
        WaterTank tank = getTankById(id);
        return adjustTankVolume(tank, litres);
    }


    // Fill tank completely
    public WaterTankResponseDTO fillTank(Long id) {
        WaterTank tank = getTankById(id);
        int litresToAdd = tank.getCapacity() - tank.getWaterVolume();
        return adjustTankVolume(tank, litresToAdd);
    }

    // Drain tank completely
    public WaterTankResponseDTO drainTank(Long id) {
        WaterTank tank = getTankById(id);
        int litresToRemove = -tank.getWaterVolume();
        return adjustTankVolume(tank, litresToRemove);
    }


    // Core method: adjust tank volume and persist
    private WaterTankResponseDTO adjustTankVolume(WaterTank tank, int delta) {
        int newVolume = Math.max(0, Math.min(tank.getCapacity(), tank.getWaterVolume() + delta));
        tank.setWaterVolume(newVolume);

        updateTankStatus(tank);
        WaterTank updated = waterTankRepository.save(tank);
        return waterTankMapper.toDisplayDTO(updated);
    }


    // Update status based on volume
    private void updateTankStatus(WaterTank tank) {
        double ratio = (double) tank.getWaterVolume() / tank.getCapacity();

        if (ratio >= 1.0) tank.setStatus(WaterLevelStatus.FULL);
        else if (ratio > 0.75) tank.setStatus(WaterLevelStatus.SUFFICIENT);
        else if (ratio > 0.5) tank.setStatus(WaterLevelStatus.MID);
        else if (ratio > 0.25) tank.setStatus(WaterLevelStatus.LOW);
        else if (ratio > 0) tank.setStatus(WaterLevelStatus.CRITICAL);
        else tank.setStatus(WaterLevelStatus.EMPTY);
    }

    // Get all tanks
    public List<WaterTankResponseDTO> getAllTanks() {
        return waterTankRepository.findAll().stream()
                .map(waterTankMapper::toDisplayDTO)
                .toList();
    }

    // Private helper: fetch by ID
    private WaterTank getTankById(Long id) {
        return waterTankRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tank doesn't exist"));
    }
}
