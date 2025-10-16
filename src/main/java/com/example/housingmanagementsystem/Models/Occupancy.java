package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.Auditable;
import com.example.housingmanagementsystem.Common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Occupancy extends BaseEntity {

    @ManyToOne
    @JoinColumn(name= "tenant_id",nullable = false )
    private User user;

    @ManyToOne
    @JoinColumn(name = "property_id",nullable = false)
    private Property property;

    @CreatedDate
    @Column(name = "date_moved_in")
    private LocalDateTime startDate;

    @LastModifiedDate
    @Column(name = "date_moved_out")
    private LocalDateTime endDate;

}
