package com.example.housingmanagementsystem.Repositories;

import com.example.housingmanagementsystem.Models.Occupancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OccupancyRepository extends JpaRepository<Occupancy,Long> {
}
