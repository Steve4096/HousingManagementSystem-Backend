package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.ReceiptResponseDTO;
import com.example.housingmanagementsystem.Services.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping("/all")
    public ResponseEntity<List<ReceiptResponseDTO>> fetchAllReceipts(){
        return ResponseEntity.ok(receiptService.fetchAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping("/(number)")
    public ResponseEntity<ReceiptResponseDTO> fetchReceiptByReceiptNumber(@RequestBody String receiptNumber){
        return ResponseEntity.ok(receiptService.fetchReceiptByNumber(receiptNumber));
    }
}
