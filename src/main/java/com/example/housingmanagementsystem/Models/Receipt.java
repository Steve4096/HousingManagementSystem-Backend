package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class Receipt extends BaseEntity {

    @Column(updatable = false,unique = true,nullable = false)
    private String receiptNumber;

    @CreatedBy
    @Column(nullable = false,updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;


    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false,unique = true)
    private Payment payment;
}
