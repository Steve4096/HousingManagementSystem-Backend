package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.Auditable;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintStatus;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintCategory;
import com.example.housingmanagementsystem.UtilityClasses.LegibilityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "complaints")
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
public class Complaint extends Auditable {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ToString.Include
    private ComplaintCategory complaintCategory;

    @NotBlank
    @Column(nullable = false)
    @ToString.Include
    private String complaintDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ComplaintStatus status=ComplaintStatus.PENDING;

    @Builder.Default
    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private LegibilityStatus legibilityStatus=LegibilityStatus.UNREAD;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="occupancy_id",nullable = false)
//    private Occupancy occupancy;
}