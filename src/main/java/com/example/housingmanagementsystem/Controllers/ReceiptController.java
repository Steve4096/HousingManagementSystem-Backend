package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.ReceiptResponseDTO;
import com.example.housingmanagementsystem.Services.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping("/all")
    public ResponseEntity<List<ReceiptResponseDTO>> fetchAllReceipts(){
        return ResponseEntity.ok(receiptService.fetchAll());
    }
}
