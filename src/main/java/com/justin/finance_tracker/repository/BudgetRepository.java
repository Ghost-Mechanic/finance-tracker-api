package com.justin.finance_tracker.repository;

import com.justin.finance_tracker.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    // A user should only have one budget per month/year
    Optional<Budget> findByUserIdAndMonthNumberAndYearNumber(Long userId, int month, int year);

    // To fetch all budgets of a user
    List<Budget> findByUserId(Long userId);
}
