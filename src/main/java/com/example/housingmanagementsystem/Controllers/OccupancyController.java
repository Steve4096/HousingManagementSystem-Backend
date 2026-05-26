package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.OccupancyResponseDTO;
import com.example.housingmanagementsystem.Services.OccupancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/occupancy")
@RequiredArgsConstructor
public class OccupancyController {

    private final OccupancyService occupancyService;

    @GetMapping("/all")
    public ResponseEntity<List<OccupancyResponseDTO>>fetchAll(){
        return ResponseEntity.ok(occupancyService.fetchAllOccupancies());
    }

    @GetMapping("/active")
    public ResponseEntity<List<OccupancyResponseDTO>> fetchActiveOccupancies(){
        return ResponseEntity.ok(occupancyService.activeOccupancies());
    }

    @GetMapping("/terminated")
    public ResponseEntity<List<OccupancyResponseDTO>> fetchTerminatedOccupancies(){
        return ResponseEntity.ok(occupancyService.terminatedOccupancies());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> terminateOccupancy(@PathVariable Long id){

        boolean deleted= occupancyService.terminateOccupancy(id);
        if (deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
