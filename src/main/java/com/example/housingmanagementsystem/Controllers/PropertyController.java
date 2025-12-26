package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.PropertyRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.PropertyResponseDTO;
import com.example.housingmanagementsystem.DTOs.SelectedPropertyDTO;
import com.example.housingmanagementsystem.Services.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService){
        this.propertyService=propertyService;
    }

    @PostMapping
    public ResponseEntity<PropertyResponseDTO> saveProperty(@Valid @RequestBody PropertyRegistrationDTO propertyRegistrationDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.createProperty(propertyRegistrationDTO));
    }

    @GetMapping
    public ResponseEntity<List<PropertyResponseDTO>> fetchExistingProperties(){
        return ResponseEntity.ok().body(propertyService.fetchExistingProperties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<SelectedPropertyDTO>> getTenantActiveProperties(){
        return ResponseEntity.ok(propertyService.getSpecificTenantActiveProperties());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id){
        boolean deleted= propertyService.deleteProperty(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
