package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.ComplaintFillingDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintResponseDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintUpdateDTO;
import com.example.housingmanagementsystem.Exceptions.AccessDeniedException;
import com.example.housingmanagementsystem.Exceptions.ConflictException;
import com.example.housingmanagementsystem.Exceptions.DuplicateException;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.ComplaintMapper;
import com.example.housingmanagementsystem.Models.Complaint;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.ComplaintRepository;
import com.example.housingmanagementsystem.Repositories.UserRepository;
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
    private final UserRepository userRepository;

    public ComplaintResponseDTO saveComplaint(ComplaintFillingDTO complaintFillingDTO){
        CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user=userRepository.findByEmailAddress(userDetails.getUsername())
                .orElseThrow(()->new RuntimeException("User not found"));

        Complaint complaint=complaintMapper.toEntity(complaintFillingDTO);
        complaint.setUser(user);
        Complaint savedComplaint=complaintRepository.save(complaint);

        return complaintMapper.toDTO(savedComplaint);
    }

    //Update a complaint only if you are the author or Admin
    public ComplaintResponseDTO editComplaint(Long id, ComplaintUpdateDTO complaintUpdateDTO){
        Complaint complaint=complaintRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Complaint not found"));

        CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();

        if(!userDetails.getUsername().equals(complaint.getUser().getEmailAddress()) && !userDetails.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
            throw new AccessDeniedException("You do not have the permission to modify this record");
        }

        //Uses mapstruct to update changed fields in the entity
        complaintMapper.updateComplaint(complaintUpdateDTO,complaint);

        Complaint savedComplaint=complaintRepository.save(complaint);

        //Returns the saved instance as a DTO
        return complaintMapper.toDTO(savedComplaint);
    }

    public ComplaintResponseDTO markAsRead(Long id){
        Complaint complaint=complaintRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Complaint not found"));

        if(complaint.getLegibilityStatus().equals(LegibilityStatus.READ)){
            return complaintMapper.toDTO(complaint);
        }

        complaint.setLegibilityStatus(LegibilityStatus.READ);
        Complaint savedComplaint=complaintRepository.save(complaint);

        return complaintMapper.toDTO(savedComplaint);
    }

    public ComplaintResponseDTO markAsSolved(Long id){
        Complaint complaint=complaintRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Complaint not found"));

        if(complaint.getStatus().equals(ComplaintStatus.SOLVED)){
            throw new ConflictException("Complaint already marked as solved");
        }

        complaint.setStatus(ComplaintStatus.SOLVED);
        Complaint savedComplaint=complaintRepository.save(complaint);

        return complaintMapper.toDTO(savedComplaint);
    }

    public List<ComplaintResponseDTO> fetchUnreadComplaints(){
        return complaintRepository.findComplaintsByLegibilityStatus(LegibilityStatus.UNREAD)
                .stream()
                .map(complaintMapper::toDTO)
                .toList();
    }

    public List<ComplaintResponseDTO> fetchPendingComplaints(){
        return complaintRepository.findComplaintsByStatus(ComplaintStatus.PENDING)
                .stream()
                .map(complaintMapper::toDTO)
                .toList();
    }

    public long countUnreadComplaints(){
        return complaintRepository.countComplaintByLegibilityStatus(LegibilityStatus.UNREAD);
    }

    public long countPendingComplaints(){
        return complaintRepository.countComplaintByStatus(ComplaintStatus.PENDING);
    }
}
