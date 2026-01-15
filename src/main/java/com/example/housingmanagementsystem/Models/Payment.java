package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.BaseEntity;
import com.example.housingmanagementsystem.UtilityClasses.LegibilityStatus;
import com.example.housingmanagementsystem.UtilityClasses.PaymentFor;
import com.example.housingmanagementsystem.UtilityClasses.PaymentMode;
import com.example.housingmanagementsystem.UtilityClasses.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = true)
@ToString(onlyExplicitlyIncluded = true)
public class Payment extends BaseEntity {


    @ToString.Include
    @Column(nullable = false,updatable = false,unique = true)
    private String transactionId;

    @ToString.Include
    @Column(nullable = false,updatable = false)
    private BigDecimal amount;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private PaymentFor paymentFor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,updatable = false)
    @NotNull
    private PaymentMode paymentMode;


    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime dateTimeOfTransaction;

    private LocalDate monthPaidFor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TransactionStatus transactionStatus=TransactionStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LegibilityStatus legibilityStatus=LegibilityStatus.UNREAD;

    @ToString.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupancy_id",nullable = false)
    private Occupancy occupancy;

    @OneToOne(mappedBy = "payment",cascade = CascadeType.ALL,orphanRemoval = true)
    private Receipt receipt;
}
