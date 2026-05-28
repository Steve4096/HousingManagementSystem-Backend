package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.PropertyRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.PropertyResponseDTO;
import com.example.housingmanagementsystem.DTOs.SelectedPropertyDTO;
import com.example.housingmanagementsystem.Services.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @PostMapping
    public ResponseEntity<PropertyResponseDTO> saveProperty(@Valid @RequestBody PropertyRegistrationDTO propertyRegistrationDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.createProperty(propertyRegistrationDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping
    public ResponseEntity<List<PropertyResponseDTO>> fetchExistingProperties(){
        return ResponseEntity.ok().body(propertyService.fetchExistingProperties());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping("/{id}")
    public ResponseEntity<List<SelectedPropertyDTO>> getTenantActiveProperties(){
        return ResponseEntity.ok(propertyService.getSpecificTenantActiveProperties());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id){
        boolean deleted= propertyService.deleteProperty(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
