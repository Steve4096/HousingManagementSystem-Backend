package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.BaseEntity;
import com.example.housingmanagementsystem.UtilityClasses.LegibilityStatus;
import com.example.housingmanagementsystem.UtilityClasses.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = true)
@ToString(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Payment extends BaseEntity {

    @ToString.Include
    @Column(nullable = false,updatable = false,unique = true)
    private String transactionId;

    @ToString.Include
    @Column(nullable = false,updatable = false)
    private BigDecimal amount;

    @ToString.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupancy_id",nullable = false)
    private Occupancy occupancy;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime dateTimeOfTransaction;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LegibilityStatus legibilityStatus=LegibilityStatus.UNREAD;

    @OneToOne(mappedBy = "payment")
    private Receipt receipt;
}
