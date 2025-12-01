package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.Auditable;
import com.example.housingmanagementsystem.UtilityClasses.WaterLevelStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
public class WaterTank extends Auditable {

    @ToString.Include
    @NotBlank(message = "You must provide a tank name/label")
    @Column(nullable = false,unique = true)
    private String tankName;

    @NotNull(message = "Each tank capacity must be declared")
    @Positive
    @Column(nullable = false)
    private int capacity;

    @PositiveOrZero
    @Column(nullable = false)
    private int waterVolume;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WaterLevelStatus status;
}
