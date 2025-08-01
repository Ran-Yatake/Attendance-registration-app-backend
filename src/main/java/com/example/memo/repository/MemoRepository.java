package com.example.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.memo.entity.Memo;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByUserId(String userId);
}
