package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.ComplaintFillingDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintResponseDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintUpdateDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.ComplaintMapper;
import com.example.housingmanagementsystem.Models.Complaint;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.ComplaintRepository;
import com.example.housingmanagementsystem.Security.CustomUserDetails;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintStatus;
import com.example.housingmanagementsystem.UtilityClasses.LegibilityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final UserService userService;

    public ComplaintResponseDTO saveComplaint(ComplaintFillingDTO complaintFillingDTO){
        CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user=userService.findUSerByEmail(userDetails.getUsername());

        Complaint complaint=complaintMapper.toEntity(complaintFillingDTO);
        complaint.setUser(user);
        Complaint savedComplaint=complaintRepository.save(complaint);

        return complaintMapper.toDTO(savedComplaint);
    }

    //Add a security check that only allows you to edit a complaint if you're the owner of the complaint or the admin
    public ComplaintResponseDTO editComplaint(Long id, ComplaintUpdateDTO complaintUpdateDTO){
        Complaint complaint=complaintRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Complaint not found"));

        //Uses mapstruct to update changed fields in the entity
        complaintMapper.updateComplaint(complaintUpdateDTO,complaint);

        Complaint savedComplaint=complaintRepository.save(complaint);

        //Returns the saved instance as a DTO
        return complaintMapper.toDTO(savedComplaint);
    }

    public List<ComplaintResponseDTO> fetchUnreadComplaints(){
        return complaintRepository.fetchUnreadComplaints(LegibilityStatus.UNREAD)
                .stream()
                .map(complaintMapper::toDTO)
                .toList();
    }

    public List<ComplaintResponseDTO> fetchPendingComplaints(){
        return complaintRepository.fetchPendingComplaints(ComplaintStatus.PENDING)
                .stream()
                .map(complaint -> complaintMapper.toDTO(complaint))
                .toList();
    }

    public long countUnreadComplaints(){
        return complaintRepository.countComplaintByLegibility(LegibilityStatus.UNREAD);
    }

    public long countPendingComplaints(){
        return complaintRepository.countComplaintByStatus(ComplaintStatus.PENDING);
    }
}
