package com.justin.finance_tracker.service;

import com.justin.finance_tracker.dto.TransactionDto;
import com.justin.finance_tracker.model.Budget;
import com.justin.finance_tracker.model.BudgetCategory;
import com.justin.finance_tracker.model.Transaction;
import com.justin.finance_tracker.repository.BudgetRepository;
import com.justin.finance_tracker.repository.TransactionRepository;
import com.justin.finance_tracker.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TransactionServiceTests {

    private TransactionRepository transactionRepository;
    private BudgetRepository budgetRepository;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        budgetRepository = mock(BudgetRepository.class);
        transactionService = new TransactionService(transactionRepository, budgetRepository);
    }

    @Test
    void testCreateTransaction_Success() {
        // given
        Budget budget = new Budget();
        budget.setId(1L);

        BudgetCategory category = new BudgetCategory();
        category.setCategoryName("Food");
        budget.setCategories(List.of(category));

        TransactionDto dto = new TransactionDto();
        dto.setTitle("Coffee");
        dto.setCategory("Food");
        dto.setAmount(BigDecimal.valueOf(3.50));
        dto.setDate(LocalDate.now());
        dto.setBudgetId(1L);

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(inv -> {
                    Transaction tx = inv.getArgument(0);
                    tx.setId(10L);
                    return tx;
                });

        // when
        TransactionDto result = transactionService.createTransaction(dto);

        // then
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getCategory()).isEqualTo("Food");

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_InvalidCategory() {
        // given
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setCategories(List.of()); // no categories

        TransactionDto dto = new TransactionDto();
        dto.setTitle("Coffee");
        dto.setCategory("Food");
        dto.setAmount(BigDecimal.valueOf(3.50));
        dto.setBudgetId(1L);

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));

        // expect
        assertThatThrownBy(() -> transactionService.createTransaction(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category not valid for this budget");
    }

    @Test
    void testGetTransactionsByBudget() {
        // given
        Transaction tx = Transaction.builder()
                .id(1L)
                .title("Coffee")
                .category("Food")
                .amount(BigDecimal.valueOf(3.50))
                .date(LocalDate.now())
                .build();

        when(transactionRepository.findByBudgetId(1L)).thenReturn(List.of(tx));

        // when
        var result = transactionService.getTransactionsByBudget(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Coffee");
    }

    @Test
    void testDeleteTransaction_Success() {
        when(transactionRepository.existsById(1L)).thenReturn(true);

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTransaction_NotFound() {
        when(transactionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> transactionService.deleteTransaction(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Transaction not found");
    }
}
