package com.example.memo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    private String userId; // Long → String に変更
}