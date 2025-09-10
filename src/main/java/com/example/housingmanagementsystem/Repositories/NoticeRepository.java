package com.example.housingmanagementsystem.Repositories;

import com.example.housingmanagementsystem.Models.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
