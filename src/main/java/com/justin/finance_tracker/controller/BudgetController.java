package com.justin.finance_tracker.controller;

import com.justin.finance_tracker.dto.BudgetDto;
import com.justin.finance_tracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/{userId}")
    public ResponseEntity<BudgetDto> createOrUpdateBudget(
            @PathVariable Long userId,
            @Valid @RequestBody BudgetDto budgetDto) {
        BudgetDto saved = budgetService.createOrUpdateBudget(userId, budgetDto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BudgetDto>> getBudgets(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getUserBudgets(userId));
    }
}
