package com.example.memo.controller;

// Memoエンティティのインポート
import com.example.memo.entity.Memo;
// Memoリポジトリのインポート
import com.example.memo.repository.MemoRepository;
// SpringのWeb関連アノテーションのインポート
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST APIのコントローラーであることを示す
@RestController
// このコントローラーのAPIパスを指定
@RequestMapping("/api/memos")
// CORS設定（Next.jsフロントエンドからのアクセスを許可）
@CrossOrigin(origins = "http://localhost:3000") // Next.jsとの連携用
public class MemoController {
    // MemoRepositoryをDI（依存性注入）で受け取る
    private final MemoRepository memoRepository;

    // コンストラクタでMemoRepositoryを初期化
    public MemoController(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    // メモ一覧取得API（GET /api/memos）
    @GetMapping
    public List<Memo> getMemos() {
        // 全メモを取得して返す
        return memoRepository.findAll();
    }

    // メモ新規作成API（POST /api/memos）
    @PostMapping
    public Memo createMemo(@RequestBody Memo memo) {
        // 受け取ったメモを保存して返す
        return memoRepository.save(memo);
    }
}
