package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.Auditable;
import com.example.housingmanagementsystem.UtilityClasses.Role;
import com.example.housingmanagementsystem.UtilityClasses.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true,callSuper = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Builder
@Audited
public class User extends Auditable {

    @ToString.Include
    @NotBlank(message = "ID number must be provided")
    @Pattern(regexp = "\\d+")
    private String idNumber;

    @ToString.Include
    @NotBlank(message = "Name cannot be blank")
    private String fullName;

    @NotNull(message = "User must be assigned a role")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ToString.Include
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must be provided")
    @Column(unique = true,nullable = false)
    private String emailAddress;

    @ToString.Include
    //@NotBlank(message = "Username must be provided")
    @Column(unique = true,nullable = true)
    private String userName;

    @Pattern(regexp = "\\+?\\d{10,15}" ,message = "Phone number must contain 10-15 digits")
    @NotBlank(message = "Phone number must be provided")
    @Column(unique = true,nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private UserStatus status;

    @ToString.Exclude
    @Column(nullable = false)
    private String passwordHash;

}
