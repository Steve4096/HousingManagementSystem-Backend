package com.example.housingmanagementsystem.Common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable extends BaseEntity{

    @CreatedDate
    @Column(name = "created_at",updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at")
    private LocalDateTime lastModifiedAt;

//    @CreatedBy
//    @Column(name = "created_by",nullable = false)
//    private String createdBy;
//
//    @LastModifiedBy
//    @Column(name = "last_updated_by",nullable = false)
//    private String lastModifiedBy;
}
