package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.CountResponseDTO;
import com.example.housingmanagementsystem.DTOs.MakePaymentDTO;
import com.example.housingmanagementsystem.DTOs.PaymentResponseDTO;
import com.example.housingmanagementsystem.Services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/make-payment")
    public ResponseEntity<PaymentResponseDTO> makePayment(@RequestBody MakePaymentDTO makePaymentDTO){
        return ResponseEntity.ok(paymentService.savePayment(makePaymentDTO));
    }

    @GetMapping("/all-count")
    public CountResponseDTO countAllPayments(){
        return new CountResponseDTO(paymentService.totalNumberOfPayments());
    }

    @GetMapping("/new-count")
    public CountResponseDTO countNewPayments(){
        return new CountResponseDTO(paymentService.countNumberOfNewPayments());
    }

    @GetMapping("/successful-count")
    public CountResponseDTO countSuccessfulPayments(){
        return new CountResponseDTO(paymentService.countSuccessfulPayments());
    }

    @GetMapping("/failed-count")
    public CountResponseDTO countFailedPayments(){
        return new CountResponseDTO(paymentService.countFailedPayments());
    }

    @GetMapping("/unread")
    public ResponseEntity<List<PaymentResponseDTO>> fetchUnreadPayments(){
        return ResponseEntity.ok(paymentService.fetchUnreadPayments());
    }

    @GetMapping("/failed")
    public ResponseEntity<List<PaymentResponseDTO>> fetchFailedPayments(){
        return ResponseEntity.ok(paymentService.fetchFailedPayments());
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> fetchAllPayments(){
        return ResponseEntity.ok(paymentService.fetchAllPayments());
    }

    @GetMapping("/successful")
    public ResponseEntity<List<PaymentResponseDTO>> fetchSuccessfulPayments(){
        return ResponseEntity.ok(paymentService.fetchSuccessfulPayments());
    }




}
