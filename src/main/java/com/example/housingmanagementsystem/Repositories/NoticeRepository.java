package com.example.housingmanagementsystem.Repositories;

import com.example.housingmanagementsystem.Models.Notice;
import com.example.housingmanagementsystem.UtilityClasses.NoticeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {
    Long countByStatus(NoticeStatus status);
    List<Notice> findByStatus(NoticeStatus status);
    boolean existsByOccupancyId(Long occupancyId);
}
