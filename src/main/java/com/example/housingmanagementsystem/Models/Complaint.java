package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.Auditable;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintStatus;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notices")
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

//    @ManyToMany
//    @JoinColumn(name = "id",nullable = false)
//    private User user;
}