package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.Auditable;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintStatus;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "complaints")
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Complaint extends Auditable {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintTypes complaintTypes;

    @NotBlank
    private String complaintDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_complaints", joinColumns = @JoinColumn(name = "complaint_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="occupancy_id",nullable = false)
    private Occupancy occupancy;
}