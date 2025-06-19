package com.example.umm_library_backend.service;

import com.example.umm_library_backend.dto.transaction.UpdateTransactionRequest;
import com.example.umm_library_backend.model.TransactionEntity;

import java.util.List;

public interface TransactionService {
    List<TransactionEntity> getAllTransactions();
    TransactionEntity partialUpdateTx(long id, UpdateTransactionRequest newData);
    void deleteTransaction(Long id);
}
