package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.ReceiptResponseDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.ReceiptMapper;
import com.example.housingmanagementsystem.Repositories.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;


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
