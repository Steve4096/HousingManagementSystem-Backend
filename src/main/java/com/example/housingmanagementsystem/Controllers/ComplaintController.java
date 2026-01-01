package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.ComplaintFillingDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintResponseDTO;
import com.example.housingmanagementsystem.DTOs.ComplaintUpdateDTO;
import com.example.housingmanagementsystem.DTOs.CountResponseDTO;
import com.example.housingmanagementsystem.Services.ComplaintService;
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
    public ResponseEntity<ComplaintResponseDTO> saveComplaint( @RequestBody ComplaintFillingDTO complaintFillingDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.saveComplaint(complaintFillingDTO));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<ComplaintResponseDTO>> fetchUnreadComplaints(){
        return ResponseEntity.ok(complaintService.fetchUnreadComplaints());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ComplaintResponseDTO>> fetchPendingComplaints(){
        return ResponseEntity.ok(complaintService.fetchPendingComplaints());
    }

    @GetMapping("/count/unread")
    public CountResponseDTO countUnreadComplaints(){
        return new CountResponseDTO(complaintService.countUnreadComplaints());
    }

    @GetMapping("/count/pending")
    public CountResponseDTO countPendingComplaints(){
        return new CountResponseDTO(complaintService.countPendingComplaints());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ComplaintResponseDTO> editComplaint(@PathVariable Long id, @RequestBody ComplaintUpdateDTO complaintUpdateDTO){
        return ResponseEntity.ok(complaintService.editComplaint(id,complaintUpdateDTO));
    }
}
