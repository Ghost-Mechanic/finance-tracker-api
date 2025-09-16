package com.justin.finance_tracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionDto {
    private Long id;
    private LocalDate date;
    private String title;
    private BigDecimal amount;
    private String category;
    private Long budgetId;
}
