package com.justin.finance_tracker.service;

import com.justin.finance_tracker.dto.BudgetCategoryDto;
import com.justin.finance_tracker.dto.BudgetDto;
import com.justin.finance_tracker.model.Budget;
import com.justin.finance_tracker.model.User;
import com.justin.finance_tracker.repository.BudgetRepository;
import com.justin.finance_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BudgetServiceTests {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    private BudgetDto budgetDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        BudgetCategoryDto categoryDto = new BudgetCategoryDto();
        categoryDto.setId(1L);
        categoryDto.setCategoryName("Food");
        categoryDto.setBudgetAmount(new BigDecimal("200"));

        budgetDto = new BudgetDto();
        budgetDto.setId(1L);
        budgetDto.setMonthNumber(5);
        budgetDto.setYearNumber(2025);
        budgetDto.setTotalBudget(new BigDecimal("1000"));
        budgetDto.setCategories(List.of(categoryDto));
    }

    @Test
    void testCreateOrUpdateBudget_NewBudget() {
        Long userId = 1L;

        // Mock userRepository to return a dummy user
        User dummyUser = new User();
        dummyUser.setId(userId);
        dummyUser.setUsername("Test User");
        when(userRepository.findById(userId)).thenReturn(Optional.of(dummyUser));

        // Mock budgetRepository behavior
        when(budgetRepository.save(any(Budget.class))).thenAnswer(invocation -> {
            Budget saved = invocation.getArgument(0);
            saved.setId(1L); // simulate DB assigning ID
            return saved;
        });

        BudgetDto result = budgetService.createOrUpdateBudget(userId, budgetDto);

        assertEquals(budgetDto.getTotalBudget(), result.getTotalBudget());
        assertEquals(budgetDto.getMonthNumber(), result.getMonthNumber());
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    void testGetUserBudgets() {
        Long userId = 1L;

        Budget budget = new Budget();
        budget.setId(1L);
        budget.setMonthNumber(5);
        budget.setYearNumber(2025);
        budget.setTotalBudget(new BigDecimal("1000"));
        budget.setCategories(new ArrayList<>());

        when(budgetRepository.findByUserId(userId)).thenReturn(Arrays.asList(budget));

        List<BudgetDto> result = budgetService.getUserBudgets(userId);

        assertEquals(1, result.size());
        assertEquals(budget.getTotalBudget(), result.get(0).getTotalBudget());
        verify(budgetRepository, times(1)).findByUserId(userId);
    }
}
