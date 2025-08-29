package com.justin.finance_tracker.controller;

import com.justin.finance_tracker.dto.TransactionDto;
import com.justin.finance_tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public TransactionDto createTransaction(@RequestBody TransactionDto dto) {
        return transactionService.createTransaction(dto);
    }

    @GetMapping("/budget/{budgetId}")
    public List<TransactionDto> getTransactionsByBudget(@PathVariable Long budgetId) {
        return transactionService.getTransactionsByBudget(budgetId);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}