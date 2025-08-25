package com.example.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.memo.entity.ExpenseReport;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long> {
    
    List<ExpenseReport> findByUserIdOrderByExpenseDateDesc(String userId);
    
    List<ExpenseReport> findByUserIdAndStatusOrderByExpenseDateDesc(String userId, String status);
    
    List<ExpenseReport> findByUserIdAndExpenseDateBetweenOrderByExpenseDateDesc(String userId, LocalDate startDate, LocalDate endDate);
    
    // 管理者用：ステータス別全ユーザーの経費申請取得
    List<ExpenseReport> findByStatusOrderByExpenseDateDesc(String status);
    
    @Query("SELECT SUM(e.amount) FROM ExpenseReport e WHERE e.userId = :userId AND e.status = :status")
    Integer getTotalAmountByUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);
    
    @Query("SELECT SUM(e.amount) FROM ExpenseReport e WHERE e.userId = :userId AND e.expenseDate BETWEEN :startDate AND :endDate")
    Integer getTotalAmountByUserIdAndDateRange(@Param("userId") String userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
