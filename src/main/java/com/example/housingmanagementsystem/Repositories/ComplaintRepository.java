package com.example.housingmanagementsystem.Repositories;

import com.example.housingmanagementsystem.Models.Complaint;
import com.example.housingmanagementsystem.UtilityClasses.ComplaintStatus;
import com.example.housingmanagementsystem.UtilityClasses.LegibilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
    long countComplaintByStatus(ComplaintStatus status);
    long countComplaintByLegibilityStatus(LegibilityStatus status);
    List<Complaint> findComplaintsByLegibilityStatus(LegibilityStatus legibilityStatus);
    List<Complaint> findComplaintsByStatus(ComplaintStatus status);
}
