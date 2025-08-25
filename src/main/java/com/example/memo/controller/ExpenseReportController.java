package com.example.memo.controller;

import com.example.memo.entity.ExpenseReport;
import com.example.memo.repository.ExpenseReportRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/expense-reports")
@CrossOrigin(origins = "*")
public class ExpenseReportController {
    private final ExpenseReportRepository expenseReportRepository;

    public ExpenseReportController(ExpenseReportRepository expenseReportRepository) {
        this.expenseReportRepository = expenseReportRepository;
    }

    // 経費申請一覧取得
    @GetMapping
    public List<ExpenseReport> getExpenseReports(@RequestParam String userId) {
        return expenseReportRepository.findByUserIdOrderByExpenseDateDesc(userId);
    }

    // ステータス別経費申請一覧取得
    @GetMapping("/status/{status}")
    public List<ExpenseReport> getExpenseReportsByStatus(@RequestParam String userId, @PathVariable String status) {
        return expenseReportRepository.findByUserIdAndStatusOrderByExpenseDateDesc(userId, status);
    }

    // 期間指定経費申請一覧取得
    @GetMapping("/period")
    public List<ExpenseReport> getExpenseReportsByPeriod(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return expenseReportRepository.findByUserIdAndExpenseDateBetweenOrderByExpenseDateDesc(userId, start, end);
    }

    // 経費申請詳細取得
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseReport> getExpenseReport(@PathVariable Long id) {
        Optional<ExpenseReport> expenseReport = expenseReportRepository.findById(id);
        return expenseReport.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 経費申請作成
    @PostMapping
    public ExpenseReport createExpenseReport(@RequestBody ExpenseReport expenseReport) {
        return expenseReportRepository.save(expenseReport);
    }

    // 経費申請更新
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseReport> updateExpenseReport(@PathVariable Long id, @RequestBody ExpenseReport updatedExpenseReport) {
        Optional<ExpenseReport> expenseReportOpt = expenseReportRepository.findById(id);
        
        if (expenseReportOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ExpenseReport expenseReport = expenseReportOpt.get();
        expenseReport.setExpenseDate(updatedExpenseReport.getExpenseDate());
        expenseReport.setDepartureLocation(updatedExpenseReport.getDepartureLocation());
        expenseReport.setArrivalLocation(updatedExpenseReport.getArrivalLocation());
        expenseReport.setTransportationMethod(updatedExpenseReport.getTransportationMethod());
        expenseReport.setPurpose(updatedExpenseReport.getPurpose());
        expenseReport.setAmount(updatedExpenseReport.getAmount());
        expenseReport.setDescription(updatedExpenseReport.getDescription());
        expenseReport.setReceiptAttached(updatedExpenseReport.getReceiptAttached());
        
        return ResponseEntity.ok(expenseReportRepository.save(expenseReport));
    }

    // 経費申請削除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseReport(@PathVariable Long id) {
        if (!expenseReportRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        expenseReportRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 合計金額取得
    @GetMapping("/total")
    public ResponseEntity<Map<String, Object>> getTotalAmount(@RequestParam String userId, @RequestParam(required = false) String status) {
        Map<String, Object> result = new HashMap<>();
        
        if (status != null) {
            Integer total = expenseReportRepository.getTotalAmountByUserIdAndStatus(userId, status);
            result.put("total", total != null ? total : 0);
            result.put("status", status);
        } else {
            Integer pendingTotal = expenseReportRepository.getTotalAmountByUserIdAndStatus(userId, "PENDING");
            Integer approvedTotal = expenseReportRepository.getTotalAmountByUserIdAndStatus(userId, "APPROVED");
            result.put("pending", pendingTotal != null ? pendingTotal : 0);
            result.put("approved", approvedTotal != null ? approvedTotal : 0);
        }
        
        return ResponseEntity.ok(result);
    }

    // 管理者用：全ユーザーの申請中経費申請一覧取得
    @GetMapping("/admin/pending")
    public List<ExpenseReport> getAllPendingExpenseReports() {
        return expenseReportRepository.findByStatusOrderByExpenseDateDesc("PENDING");
    }

    // 管理者用：経費申請承認
    @PutMapping("/admin/approve/{id}")
    public ResponseEntity<ExpenseReport> approveExpenseReport(@PathVariable Long id) {
        Optional<ExpenseReport> expenseReportOpt = expenseReportRepository.findById(id);
        
        if (expenseReportOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ExpenseReport expenseReport = expenseReportOpt.get();
        expenseReport.setStatus("APPROVED");
        
        return ResponseEntity.ok(expenseReportRepository.save(expenseReport));
    }

    // 管理者用：経費申請却下
    @PutMapping("/admin/reject/{id}")
    public ResponseEntity<ExpenseReport> rejectExpenseReport(@PathVariable Long id) {
        Optional<ExpenseReport> expenseReportOpt = expenseReportRepository.findById(id);
        
        if (expenseReportOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ExpenseReport expenseReport = expenseReportOpt.get();
        expenseReport.setStatus("REJECTED");
        
        return ResponseEntity.ok(expenseReportRepository.save(expenseReport));
    }
}
