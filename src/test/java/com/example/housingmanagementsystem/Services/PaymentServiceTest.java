package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.MakePaymentDTO;
import com.example.housingmanagementsystem.Exceptions.DuplicateException;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Models.Payment;
import com.example.housingmanagementsystem.Repositories.PaymentRepository;
import com.example.housingmanagementsystem.Repositories.ReceiptRepository;
import com.example.housingmanagementsystem.UtilityClasses.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private OccupancyService occupancyService;

    @InjectMocks
    private PaymentService paymentService;


    @Test
    void shouldGenerateTransactionIdWhenItDoesntExist() {
        //Arrange
        when(paymentRepository.existsByTransactionId(anyString()))
                .thenReturn(false);

        //Act
        String id = paymentService.generateUniqueTransactionId();

        //Assert
        assertNotNull(id);
        assertEquals(10, id.length());

        //Verify that the method was called just once
        verify(paymentRepository, times(1))
                .existsByTransactionId(anyString());
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    void shouldRetryWhenTransactionIdAlreadyExists() {
        //Arrange
        when(paymentRepository.existsByTransactionId(anyString()))
                .thenReturn(true) //Id exists on the first try
                .thenReturn(false); //Id now generated is unique

        //Act
        String id = paymentService.generateUniqueTransactionId();

        //Assert
        assertNotNull(id);

        //Verify the method was called twice
        verify(paymentRepository, times(2))
                .existsByTransactionId(anyString());
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    void shouldGenerateReceiptNumberWhenItDoesntExist() {
        //Arrange
        when(receiptRepository.existsByReceiptNumber(anyString()))
                .thenReturn(false);

        //Act
        String receiptNumber = paymentService.generateUniqueReceiptNumber();

        //Assert
        assertAll(
                () -> assertTrue(receiptNumber.startsWith("RCPT")),
                () -> assertEquals("RCPT".length() + 6, receiptNumber.length())
        );


        //Verify that the method is called just once
        verify(receiptRepository, times(1))
                .existsByReceiptNumber(anyString());
        verifyNoMoreInteractions(receiptRepository);
    }

    @Test
    void shouldRetryWhenReceiptNumberAlreadyExists() {
        //Arrange
        when(receiptRepository.existsByReceiptNumber(anyString()))
                .thenReturn(true)
                .thenReturn(false);

        //Act
        String receiptNumber = paymentService.generateUniqueReceiptNumber();

        //Assert
        assertTrue(receiptNumber.startsWith("RCPT"));
        assertEquals("RCPT".length() + 6, receiptNumber.length());

        //Verify it was called twice
        verify(receiptRepository, times(2))
                .existsByReceiptNumber(anyString());
        verifyNoMoreInteractions(receiptRepository);
    }


//    @Test
//    void shouldSavePaymentSuccessfully() {
//        //Arrange
//        MakePaymentDTO dto=new MakePaymentDTO();
//        //Payment payment=paymentMapper.toEntity(dto);
//        dto.setOccupancyId(1L);
//
//        Occupancy occupancy = new Occupancy();
//
//        when(occupancyService.findOccupancy(1L))
//                .thenReturn(Optional.of(occupancy));
//
//        when(receiptRepository.existsByReceiptNumber(anyString()))
//                .thenReturn(false);
//
//        when(paymentRepository.existsByTransactionId(anyString()))
//                .thenReturn(false);
//
//        when(paymentRepository.save(any(Payment.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        //Act
//        //Payment result = paymentService.savePayment(dto);
//
//
//
//        //Assert
//        assertAll(
//                () -> assertNotNull(result),
//                () -> assertEquals(TransactionStatus.PENDING, result.getTransactionStatus())
//        );
//
//        //Verify
//        verify(paymentRepository.save(any(Payment.class)));
//    }


    @Test
    void attemptingToSavePaymentWhenOccupancyIsnotFound() {
        //Arrange
        MakePaymentDTO dto = new MakePaymentDTO();
        dto.setOccupancyId(9L);

        when(occupancyService.findOccupancy(9L))
                .thenReturn(Optional.empty());

        //Act and assert
        assertThrows(NotFoundException.class, () -> {
            paymentService.savePayment(dto);
        });
    }

//    @Test
//    void shouldThrowDuplicateExceptionWhenReceiptAlreadyExists() {
//        //Arrange
//        MakePaymentDTO dto = new MakePaymentDTO();
//        dto.setOccupancyId(1L);
//
//        Occupancy occupancy = new Occupancy();
//
//        when(occupancyService.findOccupancy(anyLong()))
//                .thenReturn(Optional.of(occupancy));
//
//        when(receiptRepository.existsByPayment(any()))
//                .thenReturn(true);
//
//        // Act + Assert
//        assertThrows(DuplicateException.class, () -> {
//            paymentService.savePayment(dto);
//        });
//    }


}
