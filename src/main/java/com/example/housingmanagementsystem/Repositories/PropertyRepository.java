package com.example.housingmanagementsystem.Repositories;

import com.example.housingmanagementsystem.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {
        Optional<Property> findByUnitNumber(String unitNumber);
}
