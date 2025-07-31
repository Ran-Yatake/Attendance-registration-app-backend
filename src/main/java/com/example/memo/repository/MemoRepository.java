package com.example.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.memo.entity.Memo;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
