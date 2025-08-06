package com.example.memo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    
    @Column(name = "work_date")
    private String workDate; // YYYY-MM-DD形式
    
    @Column(name = "start_time")
    private LocalDateTime startTime; // 出勤時間
    
    @Column(name = "break_start_time")
    private LocalDateTime breakStartTime; // 休憩開始時間
    
    @Column(name = "break_end_time")
    private LocalDateTime breakEndTime; // 休憩終了時間
    
    @Column(name = "end_time")
    private LocalDateTime endTime; // 退勤時間
    
    private String status; // WORKING, ON_BREAK, FINISHED
}