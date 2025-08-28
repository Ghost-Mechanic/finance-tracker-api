package com.justin.finance_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetCategoryDto {
    private Long id;

    @NotBlank(message = "Category name cannot be empty")
    private String categoryName;

    @DecimalMin(value = "0.0", inclusive = false)  @NotNull(message = "Budget amount is required")
    private BigDecimal budgetAmount;
}
