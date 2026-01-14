package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class Receipt extends BaseEntity {

    @NotBlank
    @Column(updatable = false,unique = true,nullable = false)
    private String receiptNumber;

    @CreatedBy
    @NotBlank
    @Column(nullable = false,updatable = false)
    private String createdBy;

    @CreatedDate
    @NotNull
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @PositiveOrZero
    private BigDecimal balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false,unique = true)
    private Payment payment;
}
