package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.Common.IdGenerator;
import com.example.housingmanagementsystem.DTOs.MakePaymentDTO;
import com.example.housingmanagementsystem.DTOs.PaymentResponseDTO;
import com.example.housingmanagementsystem.Exceptions.DuplicateException;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.PaymentMapper;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Models.Payment;
import com.example.housingmanagementsystem.Models.Receipt;
import com.example.housingmanagementsystem.Repositories.PaymentRepository;
import com.example.housingmanagementsystem.Repositories.ReceiptRepository;
import com.example.housingmanagementsystem.UtilityClasses.LegibilityStatus;
import com.example.housingmanagementsystem.UtilityClasses.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OccupancyService occupancyService;
    private final ReceiptRepository receiptRepository;

    public String generateUniqueTransactionId() {
        String id;

        do {
            id = IdGenerator.randomString(10);
        } while (paymentRepository.existsByTransactionId(id));

        return id;
    }

    public String generateUniqueReceiptNumber() {
        String id;
        do {
            id = "RCPT" + IdGenerator.randomString(6);
        } while (receiptRepository.existsByReceiptNumber(id));

        return id;
    }


    @Transactional
    public PaymentResponseDTO savePayment(MakePaymentDTO paymentDTO){
        //Verify the occupancy exists
        Occupancy occupancy=occupancyService.findOccupancy(paymentDTO.getOccupancyId())
                .orElseThrow(()->new NotFoundException("Occupancy does not exist"));

       // Payment payment=new Payment();
        Payment payment=paymentMapper.toEntity(paymentDTO);
        payment.setOccupancy(occupancy);

        String transactionId=generateUniqueTransactionId();
        payment.setTransactionId(transactionId);
        payment.setTransactionStatus(TransactionStatus.PENDING);

        //Save the payment before extracting its ID
        Payment savedPayment=paymentRepository.save(payment);

        if(receiptRepository.existsByPayment(savedPayment)){
            throw new DuplicateException("A receipt has already been made for this payment");
        }

        Receipt receipt=new Receipt();
        String receiptNumber=generateUniqueReceiptNumber();
        receipt.setPayment(savedPayment);
        receipt.setReceiptNumber(receiptNumber);

        Receipt savedReceipt=receiptRepository.save(receipt);
        savedPayment.setReceipt(savedReceipt);

        return paymentMapper.toDTO(savedPayment);
    }

    private void markPaymentAsSuccessful(Payment payment){

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
        return paymentRepository.findAllByLegibilityStatus(LegibilityStatus.UNREAD)
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
}
