package com.example.housingmanagementsystem.Models;

import com.example.housingmanagementsystem.Common.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends Auditable {

    @Column(nullable = false,unique = true,length = 500)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean isRevoked=false;

}
