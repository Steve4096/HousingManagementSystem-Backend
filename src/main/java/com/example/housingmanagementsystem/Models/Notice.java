package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="notices")
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends Auditable {

    @NotNull
    private LocalDateTime dateIntendToLeave;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "occupancy_id",referencedColumnName = "id",nullable = false)
    private Occupancy occupancy;
}
