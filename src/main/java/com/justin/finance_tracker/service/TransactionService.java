package com.justin.finance_tracker.service;

import com.justin.finance_tracker.dto.TransactionDto;
import com.justin.finance_tracker.model.Budget;
import com.justin.finance_tracker.model.Transaction;
import com.justin.finance_tracker.repository.BudgetRepository;
import com.justin.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    public TransactionDto createTransaction(TransactionDto dto) {
        Budget budget = budgetRepository.findById(dto.getBudgetId())
                .orElseThrow(() -> new IllegalArgumentException("Budget not found"));

        boolean categoryExists = budget.getCategories().stream()
                .anyMatch(cat -> cat.getCategoryName().equalsIgnoreCase(dto.getCategory()));

        if (!categoryExists) {
            throw new IllegalArgumentException("Category not valid for this budget");
        }

        Transaction transaction = Transaction.builder()
                .title(dto.getTitle())
                .category(dto.getCategory())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .budget(budget)
                .build();

        Transaction saved = transactionRepository.save(transaction);

        dto.setId(saved.getId());
        return dto;
    }

    public List<TransactionDto> getTransactionsByBudget(Long budgetId) {
        return transactionRepository.findByBudgetId(budgetId)
                .stream()
                .map(tx -> {
                    TransactionDto dto = new TransactionDto();
                    dto.setId(tx.getId());
                    dto.setTitle(tx.getTitle());
                    dto.setCategory(tx.getCategory());
                    dto.setAmount(tx.getAmount());
                    dto.setDate(tx.getDate());
                    dto.setBudgetId(budgetId);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }
}
