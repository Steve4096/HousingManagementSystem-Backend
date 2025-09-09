package com.example.housingmanagementsystem.Models;


import com.example.housingmanagementsystem.Common.Auditable;
import com.example.housingmanagementsystem.UtilityClasses.Propertytype;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Property extends Auditable {

    @ToString.Include
    @NotBlank(message = "Each property must have a unit number")
    @Column(nullable = false,unique = true)
    private String unitNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank
    private Propertytype propertytype;

    @NotNull(message = "Rent amount is required")
    @Column(nullable = false)
    @DecimalMin(value = "8500", message = "The minimum rent amount is 8500")
    private BigDecimal rentAmount;
}
