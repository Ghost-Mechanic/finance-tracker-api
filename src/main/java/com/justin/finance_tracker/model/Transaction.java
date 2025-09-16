package com.justin.finance_tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;
    private String title;
    private BigDecimal amount;
    private String category;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;
}
