package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.Auditable;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintStatus;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintCategory;
import com.example.housingmanagementsystem.UtilityClasses.LegibilityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

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
    private ComplaintCategory complaintCategory;

    @NotBlank
    private String complaintDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ComplaintStatus status=ComplaintStatus.PENDING;

    @Builder.Default
    @Column(name = "Read/Unread")
    @NotNull
    @Enumerated(EnumType.STRING)
    private LegibilityStatus readOrUnread=LegibilityStatus.UNREAD;

   // @ManyToMany(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    //@JoinTable(name = "user_complaints", joinColumns = @JoinColumn(name = "complaint_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="occupancy_id",nullable = false)
    private Occupancy occupancy;
}