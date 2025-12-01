package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.WaterTankCreationDTO;
import com.example.housingmanagementsystem.DTOs.WaterTankSuccessfulCreationDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.WaterTankMapper;
import com.example.housingmanagementsystem.Models.WaterTank;
import com.example.housingmanagementsystem.Repositories.WaterTankRepository;
import com.example.housingmanagementsystem.UtilityClasses.WaterLevelStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaterTankService {

    private final WaterTankRepository waterTankRepository;
    private final WaterTankMapper waterTankMapper;

    public WaterTankSuccessfulCreationDTO saveWaterTankDetails(WaterTankCreationDTO tankCreationDTO){
        WaterTank waterTank=waterTankMapper.toEntity(tankCreationDTO);
        waterTank.setWaterVolume(0);
        waterTank.setStatus(WaterLevelStatus.EMPTY);
        WaterTank savedWaterTankDetails=waterTankRepository.save(waterTank);
        return waterTankMapper.toDTO(savedWaterTankDetails);
    }

    public WaterTank adjustWaterLevel(Long id,int delta){
        WaterTank tank=getTankById(id);

        //Calculate new volume
        int newVolume=tank.getWaterVolume()+delta;

        //ensure volume is within bounds
        newVolume=Math.max(0,Math.min(tank.getCapacity(),newVolume));
        tank.setWaterVolume(newVolume);

        //determine status
        determineWaterTankStatus(tank);

        //save
        return waterTankRepository.save(tank);
    }

    //Helper method to fill completely
    public WaterTank fillWaterTank(Long id){
        WaterTank tank=getTankById(id);

        int delta=tank.getCapacity() - tank.getWaterVolume();
        return adjustWaterLevel(id,delta);
    }

    //Helper method to drain completely
    public WaterTank drainWaterTank(Long id){
        WaterTank tank=getTankById(id);

        int delta=-tank.getWaterVolume();
        return adjustWaterLevel(id,delta);
    }


//    public void fillWaterTank(Long id){
//        WaterTank tank=waterTankRepository.findById(id)
//                .orElseThrow(()-> new  NotFoundException("Tank doesn't exist"));
//
//        tank.setWaterVolume(tank.getCapacity());
//        determineWaterTankStatus(tank);
//        waterTankRepository.save(tank);
//    }
//
//    public void drainWaterTank(Long id){
//        WaterTank tank=waterTankRepository.findById(id)
//                .orElseThrow(()->new NotFoundException("Tank doesn't exist"));
//
//        tank.setWaterVolume(0);
//        determineWaterTankStatus(tank);
//        waterTankRepository.save(tank);
//    }

    public WaterLevelStatus determineWaterTankStatus(WaterTank tank){
        double quotient=(double) tank.getWaterVolume()/tank.getCapacity();
        if(quotient>1) tank.setStatus(WaterLevelStatus.FULL);
         else if (quotient>0.4)  tank.setStatus(WaterLevelStatus.SUFFICIENT);
         else if (quotient>0.2) tank.setStatus(WaterLevelStatus.LOW);
         else if (quotient>0) tank.setStatus(WaterLevelStatus.CRITICAL);
         else tank.setStatus(WaterLevelStatus.EMPTY);
        return tank.getStatus();
    }

    private WaterTank getTankById(Long id){
        return waterTankRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tank doesn't exist"));
    }
}
