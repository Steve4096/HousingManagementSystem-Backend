package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Receipt extends BaseEntity {

    @Column(updatable = false,unique = true,nullable = false)
    private String receiptNumber;

    @CreatedBy
    @Column(nullable = false,updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false,unique = true)
    private Payment payment;
}
