package com.justin.finance_tracker.controller;

import com.justin.finance_tracker.dto.BudgetCategoryDto;
import com.justin.finance_tracker.dto.BudgetDto;
import com.justin.finance_tracker.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BudgetControllerTests {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private BudgetDto budgetDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        BudgetCategoryDto category = new BudgetCategoryDto();
        category.setId(1L);
        category.setCategoryName("Food");
        category.setBudgetAmount(new BigDecimal("200"));

        budgetDto = new BudgetDto();
        budgetDto.setId(1L);
        budgetDto.setMonthNumber(5);
        budgetDto.setYearNumber(2025);
        budgetDto.setTotalBudget(new BigDecimal("1000"));
        budgetDto.setCategories(List.of(category));
    }

    @Test
    void testCreateOrUpdateBudget() {
        Long userId = 1L;
        when(budgetService.createOrUpdateBudget(userId, budgetDto)).thenReturn(budgetDto);

        ResponseEntity<BudgetDto> response = budgetController.createOrUpdateBudget(userId, budgetDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(budgetDto, response.getBody());
        verify(budgetService, times(1)).createOrUpdateBudget(userId, budgetDto);
    }

    @Test
    void testGetBudgets() {
        Long userId = 1L;
        List<BudgetDto> budgetList = Arrays.asList(budgetDto);
        when(budgetService.getUserBudgets(userId)).thenReturn(budgetList);

        ResponseEntity<List<BudgetDto>> response = budgetController.getBudgets(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(budgetDto, response.getBody().get(0));
        verify(budgetService, times(1)).getUserBudgets(userId);
    }
}
