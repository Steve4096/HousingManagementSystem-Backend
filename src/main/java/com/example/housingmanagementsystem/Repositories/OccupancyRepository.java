package com.example.housingmanagementsystem.Repositories;

import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OccupancyRepository extends JpaRepository<Occupancy,Long> {
    boolean existsByPropertyAndEndDateIsNull(Property property);
    List<Occupancy> findByEndDateIsNull();
    List<Occupancy> findByEndDateIsNotNull();
}
