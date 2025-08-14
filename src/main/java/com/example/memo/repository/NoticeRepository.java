package com.example.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.memo.entity.Notice;
import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    
    @Query("SELECT n FROM Notice n WHERE n.isActive = true AND " +
           "(n.startDate IS NULL OR n.startDate <= :now) AND " +
           "(n.endDate IS NULL OR n.endDate >= :now) " +
           "ORDER BY n.createdAt DESC")
    List<Notice> findActiveNotices(LocalDateTime now);
    
    List<Notice> findByIsActiveTrueOrderByCreatedAtDesc();
    
    @Query("SELECT n FROM Notice n ORDER BY n.createdAt DESC")
    List<Notice> findAllOrderByCreatedAtDesc();
}
