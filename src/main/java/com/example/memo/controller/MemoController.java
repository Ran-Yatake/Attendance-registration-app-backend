package com.example.memo.controller;

import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/memos")
@CrossOrigin(origins = "*") // CORS許可
public class MemoController {
    private final MemoRepository memoRepository;

    public MemoController(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    // メモ一覧取得（GET）: ユーザーIDで絞り込み
    @GetMapping
    public List<Memo> getMemos(@RequestParam String userId) { // Long → String
        return memoRepository.findByUserId(userId);
    }

    // メモ新規作成（POST）
    @PostMapping
    public Memo createMemo(@RequestBody Memo memo) {
        return memoRepository.save(memo);
    }
}
