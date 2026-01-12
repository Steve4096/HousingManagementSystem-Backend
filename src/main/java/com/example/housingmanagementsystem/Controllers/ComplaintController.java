package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.ComplaintFillingDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintResponseDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintUpdateDTO;
import com.example.housingmanagementsystem.DTOs.CountResponseDTO;
import com.example.housingmanagementsystem.Services.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ComplaintResponseDTO> saveComplaint( @Valid @RequestBody ComplaintFillingDTO complaintFillingDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.saveComplaint(complaintFillingDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping("/unread")
    public ResponseEntity<List<ComplaintResponseDTO>> fetchUnreadComplaints(){
        return ResponseEntity.ok(complaintService.fetchUnreadComplaints());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping("/pending")
    public ResponseEntity<List<ComplaintResponseDTO>> fetchPendingComplaints(){
        return ResponseEntity.ok(complaintService.fetchPendingComplaints());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping("/count/unread")
    public CountResponseDTO countUnreadComplaints(){
        return new CountResponseDTO(complaintService.countUnreadComplaints());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping("/count/pending")
    public CountResponseDTO countPendingComplaints(){
        return new CountResponseDTO(complaintService.countPendingComplaints());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @PatchMapping("/{id}/read")
    public ResponseEntity<ComplaintResponseDTO> markAsRead(@PathVariable Long id){
        return ResponseEntity.ok(complaintService.markAsRead(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @PatchMapping("/{id}/solved")
    public ResponseEntity<ComplaintResponseDTO> markAsSolved(@PathVariable Long id){
        return ResponseEntity.ok(complaintService.markAsSolved(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ComplaintResponseDTO> editComplaint(@PathVariable Long id, @RequestBody ComplaintUpdateDTO complaintUpdateDTO){
        return ResponseEntity.ok(complaintService.editComplaint(id,complaintUpdateDTO));
    }
}
