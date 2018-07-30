package com.example.demo.repository;

import com.example.demo.data.HistoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryInfoRepository extends JpaRepository<HistoryInfo, Long> {
}
