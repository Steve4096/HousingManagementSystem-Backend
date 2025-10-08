package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.PropertyRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.PropertyResponseDTO;
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

    public PropertyService(PropertyRepository propertyRepository,PropertyMapper propertyMapper){
        this.propertyRepository=propertyRepository;
        this.propertyMapper=propertyMapper;
    }

    public PropertyResponseDTO createProperty(PropertyRegistrationDTO registrationDTO){
        if(propertyRepository.findByUnitNumber(registrationDTO.getUnitNumber()).isPresent()){
            throw new DuplicateException("A property of the same unit number already exists");
        }

        //Converting the DTO to an Entity
        Property property= propertyMapper.toEntity(registrationDTO);

        //Save the generated entity
        Property savedProperty=propertyRepository.save(property);

        //Return the response in DTO form
        return propertyMapper.toDTO(savedProperty);
    }

    public List<PropertyResponseDTO> fetchExistingProperties(){
        return propertyRepository.findAll()
                .stream()
                .map(propertyMapper::toDTO)
                .toList();
    }

    public boolean deleteProperty(Long id){
        boolean exists=propertyRepository.existsById(id);
        if(exists){
            propertyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
