package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.Common.IdGenerator;
import com.example.housingmanagementsystem.DTOs.CountResponseDTO;
import com.example.housingmanagementsystem.DTOs.MakePaymentDTO;
import com.example.housingmanagementsystem.DTOs.PaymentResponseDTO;
import com.example.housingmanagementsystem.DTOs.ReceiptCreationDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.PaymentMapper;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Models.Payment;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.PaymentRepository;
import com.example.housingmanagementsystem.Security.CustomUserDetails;
import com.example.housingmanagementsystem.UtilityClasses.LegibilityStatus;
import com.example.housingmanagementsystem.UtilityClasses.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserService userService;
    private final OccupancyService occupancyService;
    private final ReceiptService receiptService;

    public String generateUniqueTransactionId() {
        String id;

        do {
            id = IdGenerator.randomString(10);
        } while (paymentRepository.existsByTransactionId(id));

        return id;
    }


    @Transactional
    public PaymentResponseDTO savePayment(MakePaymentDTO paymentDTO){
        //Get currently logged-in user from spring security
        CustomUserDetails userDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        User user=userService.findUSerByEmail(userDetails.getUsername());

        //Verify the occupancy exists
        Occupancy occupancy=occupancyService.findOccupancy(paymentDTO.getOccupancyId())
                .orElseThrow(()->new NotFoundException("Occupancy does not exist"));



        //Setting the occupancy
       // Payment payment=new Payment();
        Payment payment=paymentMapper.toEntity(paymentDTO);
        payment.setOccupancy(occupancy);

        String transactionId=generateUniqueTransactionId();
        payment.setTransactionId(transactionId);
        payment.setLegibilityStatus(LegibilityStatus.UNREAD);


        //Save the payment before extracting its ID
        Payment savedPayment=paymentRepository.save(payment);

        //Pass the payment ID to the receipt creation DTO
//        ReceiptCreationDTO receiptCreationDTO=new ReceiptCreationDTO();
//        Long paymentId=savedPayment.getId();
//        receiptCreationDTO.setPaymentId(paymentId);
//
//        //Save the receipt
//        receiptService.saveReceipt(receiptCreationDTO);
        receiptService.saveReceipt(new ReceiptCreationDTO(savedPayment.getId()));

        return paymentMapper.toDTO(savedPayment);
    }

    public long totalNumberOfPayments(){
        return paymentRepository.count();
    }

    public long countNumberOfNewPayments(){
        return paymentRepository.countByLegibilityStatus(LegibilityStatus.UNREAD);
    }

    public Long countFailedPayments(){
        return paymentRepository.countByTransactionStatus(TransactionStatus.FAILED);
    }

    public long countSuccessfulPayments(){
        return paymentRepository.countByTransactionStatus(TransactionStatus.SUCCESSFUL);
    }

    public List<PaymentResponseDTO> fetchAllPayments(){
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDTO)
                .toList();
    }

    public List<PaymentResponseDTO> fetchUnreadPayments(){
        return paymentRepository.findAllByLegiblityStatus(LegibilityStatus.UNREAD)
                .stream()
                .map(paymentMapper::toDTO)
                .toList();
    }

    public List<PaymentResponseDTO> fetchFailedPayments(){
        return paymentRepository.findAllByTransactionStatus(TransactionStatus.FAILED)
                .stream()
                .map(paymentMapper::toDTO)
                .toList();
    }

    public List<PaymentResponseDTO> fetchSuccessfulPayments(){
        return paymentRepository.findAllByTransactionStatus(TransactionStatus.SUCCESSFUL)
                .stream()
                .map(paymentMapper::toDTO)
                .toList();
    }

    public Payment findById(Long id){
        return paymentRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Payment not found"));
    }

}
