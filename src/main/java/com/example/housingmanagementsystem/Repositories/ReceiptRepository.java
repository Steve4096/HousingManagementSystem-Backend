package com.example.housingmanagementsystem.Repositories;

import com.example.housingmanagementsystem.Models.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt,Long> {
    Optional<Receipt> findByReceiptNumber(String receiptNumber);
    boolean existsByReceiptNumber(String receiptNumber);
}
