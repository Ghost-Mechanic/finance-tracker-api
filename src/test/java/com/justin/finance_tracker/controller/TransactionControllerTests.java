package com.justin.finance_tracker.controller;

import com.justin.finance_tracker.controller.TransactionController;
import com.justin.finance_tracker.dto.TransactionDto;
import com.justin.finance_tracker.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTests {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionDto transactionDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        transactionDto = new TransactionDto();
        transactionDto.setId(1L);
        transactionDto.setTitle("Coffee");
        transactionDto.setCategory("Food");
        transactionDto.setAmount(BigDecimal.valueOf(3.50));
        transactionDto.setDate(LocalDate.of(2025, 8, 28));
        transactionDto.setBudgetId(1L);
    }

    @Test
    void testCreateTransaction() {
        when(transactionService.createTransaction(transactionDto)).thenReturn(transactionDto);

        TransactionDto response = transactionController.createTransaction(transactionDto);

        assertEquals(transactionDto, response);
        verify(transactionService, times(1)).createTransaction(transactionDto);
    }

    @Test
    void testGetTransactionsByBudget() {
        List<TransactionDto> transactions = Arrays.asList(transactionDto);
        when(transactionService.getTransactionsByBudget(1L)).thenReturn(transactions);

        List<TransactionDto> response = transactionController.getTransactionsByBudget(1L);

        assertEquals(1, response.size());
        assertEquals(transactionDto, response.get(0));
        verify(transactionService, times(1)).getTransactionsByBudget(1L);
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionService).deleteTransaction(1L);

        transactionController.deleteTransaction(1L);

        verify(transactionService, times(1)).deleteTransaction(1L);
    }
}
