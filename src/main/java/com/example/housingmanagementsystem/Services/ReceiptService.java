package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.Common.IdGenerator;
import com.example.housingmanagementsystem.DTOs.ReceiptCreationDTO;
import com.example.housingmanagementsystem.DTOs.ReceiptResponseDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.ReceiptMapper;
import com.example.housingmanagementsystem.Models.Payment;
import com.example.housingmanagementsystem.Models.Receipt;
import com.example.housingmanagementsystem.Repositories.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final PaymentService paymentService;

    public String generateUniqueReceiptNumber() {
        String id;
        do {
            id = "RCPT" + IdGenerator.randomString(6);
        } while (receiptRepository.existsByReceiptNumber(id));

        return id;
    }


    @Transactional
    public ReceiptResponseDTO saveReceipt(ReceiptCreationDTO receiptCreationDTO){
        Payment payment=paymentService.findById(receiptCreationDTO.getPaymentId());
        String receiptNumber=generateUniqueReceiptNumber();

        Receipt receipt=new Receipt();
        receipt.setReceiptNumber(receiptNumber);
        receipt.setPayment(payment);

        //Save the receipt
        Receipt savedReceipt=receiptRepository.save(receipt);

        return receiptMapper.toDTO(savedReceipt);
    }

    public List<ReceiptResponseDTO> fetchAll(){
        return receiptRepository.findAll().stream()
                .map(receiptMapper::toDTO)
                .toList();
    }

    public ReceiptResponseDTO fetchReceiptByNumber(String receiptNumber){
        //Optional<Receipt> receipt=receiptRepository.fetchByReceiptNumber(receiptNumber);
        return receiptRepository.findByReceiptNumber(receiptNumber)
                .map(receiptMapper::toDTO)
                .orElseThrow(()->new NotFoundException("Receipt not found"));
    }
}
