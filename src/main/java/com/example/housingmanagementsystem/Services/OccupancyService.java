package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.OccupancyCreationDTO;
import com.example.housingmanagementsystem.DTOs.OccupancyResponseDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.OccupancyMapper;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Models.Property;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.OccupancyRepository;
import com.example.housingmanagementsystem.Repositories.PropertyRepository;
import com.example.housingmanagementsystem.Repositories.UserRepository;
import com.example.housingmanagementsystem.Security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OccupancyService {

    private final OccupancyRepository occupancyRepository;
    private final OccupancyMapper occupancyMapper;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final UserService userService;

    public OccupancyResponseDTO createOccupancy(OccupancyCreationDTO creationDTO){
        Long userId=creationDTO.getUserId();
        Long propertyId= creationDTO.getPropertyId();

        User newUser=userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));

        Property newProperty=propertyRepository.findById(propertyId)
                .orElseThrow(()->new NotFoundException("Property not found"));

        Occupancy occupancy=new Occupancy();
        occupancy.setUser(newUser);
        occupancy.setProperty(newProperty);

        Occupancy savedOcccupancy=occupancyRepository.save(occupancy);

        return occupancyMapper.toDTO(savedOcccupancy);
    }

    public List<OccupancyResponseDTO> fetchAllOccupancies(){
        return occupancyRepository.findAll()
                .stream()
                .map(occupancyMapper::toDTO)
                .toList();
    }

    public List<OccupancyResponseDTO> findLoggedInUserOccupancy(){
        CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser=userService.findUSerByEmail(userDetails.getUsername());

        return loggedInUser.getOccupancies().stream()
                .map(occupancyMapper::toDTO)
                .toList();
    }

    public Optional<Occupancy> findOccupancy(Long id){
        return occupancyRepository.findById(id);
    }
}
