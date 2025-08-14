package com.example.memo.controller;

import com.example.memo.entity.Notice;
import com.example.memo.repository.NoticeRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notices")
@CrossOrigin(origins = "*")
public class NoticeController {
    private final NoticeRepository noticeRepository;

    public NoticeController(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // アクティブなお知らせ一覧取得（フロントエンド用）
    @GetMapping("/active")
    public List<Notice> getActiveNotices() {
        return noticeRepository.findActiveNotices(LocalDateTime.now());
    }

    // 全お知らせ一覧取得（管理用）
    @GetMapping
    public List<Notice> getAllNotices() {
        return noticeRepository.findAllOrderByCreatedAtDesc();
    }

    // お知らせ詳細取得
    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNotice(@PathVariable Long id) {
        Optional<Notice> notice = noticeRepository.findById(id);
        return notice.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // お知らせ作成
    @PostMapping
    public Notice createNotice(@RequestBody Notice notice) {
        return noticeRepository.save(notice);
    }

    // お知らせ更新
    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(@PathVariable Long id, @RequestBody Notice updatedNotice) {
        Optional<Notice> noticeOpt = noticeRepository.findById(id);
        
        if (noticeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Notice notice = noticeOpt.get();
        notice.setTitle(updatedNotice.getTitle());
        notice.setContent(updatedNotice.getContent());
        notice.setIsActive(updatedNotice.getIsActive());
        notice.setStartDate(updatedNotice.getStartDate());
        notice.setEndDate(updatedNotice.getEndDate());
        
        return ResponseEntity.ok(noticeRepository.save(notice));
    }

    // お知らせ削除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        if (!noticeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        noticeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
