package com.justin.finance_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "budget_categories",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"budget_id", "categoryName"})
    })
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetCategory {
    @Id
    @GeneratedValue
    private Long id;

    private String categoryName;

    @Column(precision = 19, scale = 2)
    private BigDecimal budgetAmount;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;
}
