package com.example.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.memo.entity.Attendance;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUserId(String userId);
    
    List<Attendance> findByUserIdOrderByWorkDateDesc(String userId);
    
    Optional<Attendance> findByUserIdAndWorkDate(String userId, String workDate);
    
    @Query("SELECT a FROM Attendance a WHERE a.userId = :userId AND a.workDate = :workDate")
    Optional<Attendance> findTodayAttendance(@Param("userId") String userId, @Param("workDate") String workDate);
}
