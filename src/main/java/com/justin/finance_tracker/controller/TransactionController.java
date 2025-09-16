package com.justin.finance_tracker.controller;

import com.justin.finance_tracker.dto.TransactionDto;
import com.justin.finance_tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto dto) {
        TransactionDto saved =  transactionService.createTransaction(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/budget/{budgetId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByBudget(@PathVariable Long budgetId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBudget(budgetId));
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}