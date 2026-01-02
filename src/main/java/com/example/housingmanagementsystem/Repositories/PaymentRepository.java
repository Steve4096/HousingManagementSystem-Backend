package com.example.housingmanagementsystem.Repositories;

import com.example.housingmanagementsystem.Models.Payment;
import com.example.housingmanagementsystem.UtilityClasses.LegibilityStatus;
import com.example.housingmanagementsystem.UtilityClasses.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    long countByLegibilityStatus(LegibilityStatus legibilityStatus);
    long countByTransactionStatus(TransactionStatus status);
    List<Payment> findAllByLegiblityStatus(LegibilityStatus status);
    List<Payment> findAllByTransactionStatus(TransactionStatus status);
    boolean existsByTransactionId(String id);
}
