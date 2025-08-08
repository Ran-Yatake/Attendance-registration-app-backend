package com.example.memo.controller;

import com.example.memo.entity.Attendance;
import com.example.memo.repository.AttendanceRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*") // CORS許可
public class AttendanceController {
    private final AttendanceRepository attendanceRepository;

    public AttendanceController(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // 勤怠記録一覧取得（GET）: ユーザーIDで絞り込み
    @GetMapping
    public List<Attendance> getAttendanceRecords(@RequestParam String userId) {
        return attendanceRepository.findByUserIdOrderByWorkDateDesc(userId);
    }

    // 今日の勤怠記録取得（GET）
    @GetMapping("/today")
    public ResponseEntity<Attendance> getTodayAttendance(@RequestParam String userId) {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Optional<Attendance> attendance = attendanceRepository.findTodayAttendance(userId, today);
        
        if (attendance.isPresent()) {
            return ResponseEntity.ok(attendance.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 出勤打刻（POST）
    @PostMapping("/start")
    public ResponseEntity<Attendance> startWork(@RequestParam String userId) {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Optional<Attendance> existingAttendance = attendanceRepository.findByUserIdAndWorkDate(userId, today);
        
        if (existingAttendance.isPresent()) {
            return ResponseEntity.badRequest().build(); // 既に出勤済み
        }
        
        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setWorkDate(today);
        attendance.setStartTime(LocalDateTime.now());
        attendance.setStatus("WORKING");
        
        return ResponseEntity.ok(attendanceRepository.save(attendance));
    }

    // 休憩開始打刻（PUT）
    @PutMapping("/break-start")
    public ResponseEntity<Attendance> startBreak(@RequestParam String userId) {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Optional<Attendance> attendanceOpt = attendanceRepository.findTodayAttendance(userId, today);
        
        if (attendanceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Attendance attendance = attendanceOpt.get();
        if (!"WORKING".equals(attendance.getStatus())) {
            return ResponseEntity.badRequest().build();
        }
        
        attendance.setBreakStartTime(LocalDateTime.now());
        attendance.setStatus("ON_BREAK");
        
        return ResponseEntity.ok(attendanceRepository.save(attendance));
    }

    // 休憩終了打刻（PUT）
    @PutMapping("/break-end")
    public ResponseEntity<Attendance> endBreak(@RequestParam String userId) {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Optional<Attendance> attendanceOpt = attendanceRepository.findTodayAttendance(userId, today);
        
        if (attendanceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Attendance attendance = attendanceOpt.get();
        if (!"ON_BREAK".equals(attendance.getStatus())) {
            return ResponseEntity.badRequest().build();
        }
        
        attendance.setBreakEndTime(LocalDateTime.now());
        attendance.setStatus("WORKING");
        
        return ResponseEntity.ok(attendanceRepository.save(attendance));
    }

    // 退勤打刻（PUT）
    @PutMapping("/end")
    public ResponseEntity<Attendance> endWork(@RequestParam String userId) {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Optional<Attendance> attendanceOpt = attendanceRepository.findTodayAttendance(userId, today);
        
        if (attendanceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Attendance attendance = attendanceOpt.get();
        if ("FINISHED".equals(attendance.getStatus())) {
            return ResponseEntity.badRequest().build();
        }
        
        attendance.setEndTime(LocalDateTime.now());
        attendance.setStatus("FINISHED");
        
        return ResponseEntity.ok(attendanceRepository.save(attendance));
    }

    // 勤怠記録編集（PUT）
    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @RequestBody Attendance updatedAttendance) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findById(id);
        
        if (attendanceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Attendance attendance = attendanceOpt.get();
        
        // 更新可能なフィールドのみ更新
        if (updatedAttendance.getStartTime() != null) {
            attendance.setStartTime(updatedAttendance.getStartTime());
        }
        if (updatedAttendance.getBreakStartTime() != null) {
            attendance.setBreakStartTime(updatedAttendance.getBreakStartTime());
        }
        if (updatedAttendance.getBreakEndTime() != null) {
            attendance.setBreakEndTime(updatedAttendance.getBreakEndTime());
        }
        if (updatedAttendance.getEndTime() != null) {
            attendance.setEndTime(updatedAttendance.getEndTime());
        }
        if (updatedAttendance.getStatus() != null) {
            attendance.setStatus(updatedAttendance.getStatus());
        }
        
        return ResponseEntity.ok(attendanceRepository.save(attendance));
    }

    // 勤怠記録削除（DELETE）
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        if (!attendanceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        attendanceRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
