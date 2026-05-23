package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.OccupancyResponseDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.OccupancyMapper;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.OccupancyRepository;
import com.example.housingmanagementsystem.Repositories.UserRepository;
import com.example.housingmanagementsystem.Security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OccupancyService {

    private final OccupancyRepository occupancyRepository;
    private final OccupancyMapper occupancyMapper;
    private final PropertyService propertyService;
    private final UserRepository userRepository;

    //public OccupancyResponseDTO createOccupancy(){}

    public List<OccupancyResponseDTO> fetchAllOccupancies(){
        return occupancyRepository.findAll()
                .stream()
                .map(occupancyMapper::toDTO)
                .toList();
    }

    public List<OccupancyResponseDTO> findLoggedInUserOccupancy(){
        CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser=userRepository.findByEmailAddress(userDetails.getUsername())
                .orElseThrow(()->new RuntimeException("User not found"));

        return loggedInUser.getOccupancies().stream()
                .map(occupancyMapper::toDTO)
                .toList();
    }

    @Transactional
    public void terminateOccupancy(Long occupancyId){
        Occupancy occupancy=occupancyRepository.findById(occupancyId)
                .orElseThrow(()->new NotFoundException("Occupancy record does not exist"));

        occupancy.setEndDate(LocalDateTime.now());
        //User user=occupancy.getUser();
        //user.setStatus(UserStatus.INACTIVE);
        occupancyRepository.save(occupancy);
    }

    public Optional<Occupancy> findOccupancy(Long id){
        return occupancyRepository.findById(id);
    }
}
