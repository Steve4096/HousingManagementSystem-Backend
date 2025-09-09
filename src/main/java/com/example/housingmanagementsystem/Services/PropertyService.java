package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.PropertyRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.PropertyResponseDTO;
import com.example.housingmanagementsystem.DTOs.PropertyUpdateDTO;
import com.example.housingmanagementsystem.Exceptions.DuplicateException;
import com.example.housingmanagementsystem.Mappers.PropertyMapper;
import com.example.housingmanagementsystem.Models.Property;
import com.example.housingmanagementsystem.Repositories.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyService(PropertyRepository propertyRepository, PropertyMapper propertyMapper){
        this.propertyRepository=propertyRepository;
        this.propertyMapper=propertyMapper;
    }

    public PropertyResponseDTO createProperty(PropertyRegistrationDTO propertyRegistrationDTO){
        if(propertyRepository.findByUnitNumber(propertyRegistrationDTO.getUnitNumber()).isPresent()){
            throw new DuplicateException("A property of the same unit number exists.Please enter another unit number");
        }

        //Convert the DTO to an entity
        Property property=propertyMapper.toEntity(propertyRegistrationDTO);

        //Save the DTO that has already been mapped to an entity
        Property savedProperty=propertyRepository.save(property);

        //Returning the result in DTO form
        return propertyMapper.toDTO(savedProperty);
    }

    public PropertyResponseDTO updateExistingPropertyDetails(Long id,PropertyUpdateDTO propertyUpdateDTO){
        Property property=propertyRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Property not found"));

        //Using Mapstruct to track changes
        propertyMapper.updatePropertyEntity(propertyUpdateDTO,property);

        //Save changes made
        Property updatedProperty=propertyRepository.save(property);

        //Return DTO response
        return propertyMapper.toDTO(updatedProperty);
    }

    public List<PropertyResponseDTO> fetchExistingProperties(){
        return propertyRepository.findAll()
                .stream()
                .map(propertyMapper::toDTO)
                .toList();
    }

    //public boolean checkIfUnitNumberExists(){}

    public boolean deleteProperty(Long id){
        boolean exists=propertyRepository.existsById(id);
        if(exists){
            propertyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

