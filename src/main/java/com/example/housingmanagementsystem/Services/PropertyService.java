package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.PropertyRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.PropertyResponseDTO;
import com.example.housingmanagementsystem.DTOs.SelectedPropertyDTO;
import com.example.housingmanagementsystem.Exceptions.DuplicateException;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.PropertyMapper;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Models.Property;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.PropertyRepository;
import com.example.housingmanagementsystem.Security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final UserService userService;

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

    public List<SelectedPropertyDTO> getSpecificTenantActiveProperties(){
        CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userService.findUSerByEmail(userDetails.getUsername());

        return user.getOccupancies().stream()
                .filter(o->o.getEndDate()==null)
                //.map(Occupancy::getProperty)//Convert Occupancy to property
                .map(propertyMapper::selectedPropertyToDTO)
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
