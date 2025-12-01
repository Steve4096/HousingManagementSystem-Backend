package com.example.housingmanagementsystem.Repositories;

import com.example.housingmanagementsystem.Models.WaterTank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterTankRepository extends JpaRepository<WaterTank,Long> {
}
