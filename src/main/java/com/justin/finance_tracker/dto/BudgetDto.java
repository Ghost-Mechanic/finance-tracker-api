package com.justin.finance_tracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class BudgetDto {
    private Long id;

    @Min(1) @Max(12) @NotNull(message = "Month required")
    private int monthNumber;

    @Min(2020) @Max(2026) @NotNull(message = "Year required")
    private int yearNumber;

    @DecimalMin(value = "0.0", inclusive = false) @NotNull(message = "Budget required")
    private BigDecimal totalBudget;

    private List<BudgetCategoryDto> categories = new ArrayList<>();
}
