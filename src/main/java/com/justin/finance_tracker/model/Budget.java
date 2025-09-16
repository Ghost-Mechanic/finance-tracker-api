package com.justin.finance_tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "budgets")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {
    @Id
    @GeneratedValue
    private Long id;

    private int monthNumber;
    private int yearNumber;

    @Column(precision = 19, scale = 2)
    private BigDecimal totalBudget;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Include the categories
    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetCategory> categories = new ArrayList<>();
}
