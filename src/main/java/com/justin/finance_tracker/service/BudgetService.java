package com.justin.finance_tracker.service;

import com.justin.finance_tracker.dto.BudgetCategoryDto;
import com.justin.finance_tracker.dto.BudgetDto;
import com.justin.finance_tracker.model.Budget;
import com.justin.finance_tracker.model.BudgetCategory;
import com.justin.finance_tracker.model.User;
import com.justin.finance_tracker.repository.BudgetCategoryRepository;
import com.justin.finance_tracker.repository.BudgetRepository;
import com.justin.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetCategoryRepository categoryRepository;
    private final UserRepository userRepository; // assuming you already have this

    // Create or update a budget
    public BudgetDto createOrUpdateBudget(Long userId, BudgetDto budgetDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if budget already exists for month/year
        Budget budget = budgetRepository.findByUserIdAndMonthNumberAndYearNumber(userId, budgetDto.getMonthNumber(), budgetDto.getYearNumber())
                .orElse(new Budget());

        budget.setUser(user);
        budget.setMonthNumber(budgetDto.getMonthNumber());
        budget.setYearNumber(budgetDto.getYearNumber());
        budget.setTotalBudget(budgetDto.getTotalBudget());

        // Map categories
        List<BudgetCategory> categories = budgetDto.getCategories().stream()
                .map(dto -> BudgetCategory.builder()
                        .id(dto.getId()) // null if new
                        .categoryName(dto.getCategoryName())
                        .budgetAmount(dto.getBudgetAmount())
                        .budget(budget)
                        .build())
                .collect(Collectors.toList());

        // Validation: check categories total
        BigDecimal totalCategories = categories.stream()
                .map(BudgetCategory::getBudgetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalCategories.compareTo(budgetDto.getTotalBudget()) > 0) {
            throw new IllegalArgumentException("Categories exceed total budget");
        }
        else if (budgetDto.getCategories().size() > 10) {
            throw new IllegalArgumentException("A budget cannot have more than 10 categories");
        }

        budget.getCategories().clear();
        budget.getCategories().addAll(categories);

        Budget saved = budgetRepository.save(budget);

        return mapToDto(saved);
    }

    public List<BudgetDto> getUserBudgets(Long userId) {
        return budgetRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    private BudgetDto mapToDto(Budget budget) {
        BudgetDto dto = new BudgetDto();
        dto.setId(budget.getId());
        dto.setMonthNumber(budget.getMonthNumber());
        dto.setYearNumber(budget.getYearNumber());
        dto.setTotalBudget(budget.getTotalBudget());
        dto.setCategories(
                budget.getCategories().stream()
                        .map(cat -> {
                            BudgetCategoryDto catDto = new BudgetCategoryDto();
                            catDto.setId(cat.getId());
                            catDto.setCategoryName(cat.getCategoryName());
                            catDto.setBudgetAmount(cat.getBudgetAmount());
                            return catDto;
                        })
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
