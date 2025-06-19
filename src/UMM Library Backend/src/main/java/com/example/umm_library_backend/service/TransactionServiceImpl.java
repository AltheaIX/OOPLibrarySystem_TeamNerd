package com.example.umm_library_backend.service;

import com.example.umm_library_backend.dto.transaction.UpdateTransactionRequest;
import com.example.umm_library_backend.dto.user.UpdateUserRequest;
import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.model.TransactionEntity;
import com.example.umm_library_backend.model.UsersEntity;
import com.example.umm_library_backend.repository.TransactionRepositoryImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepositoryImpl transactionRepositoryImpl;

    public TransactionServiceImpl(TransactionRepositoryImpl transactionRepositoryImpl) {
        this.transactionRepositoryImpl = transactionRepositoryImpl;
    }

    public List<TransactionEntity> getAllTransactions() {
        return transactionRepositoryImpl.findAll();
    }

    public TransactionEntity partialUpdateTx(long id, UpdateTransactionRequest newData) {
        List<TransactionEntity> transactions = transactionRepositoryImpl.findById(id);
        if (transactions == null) {
            throw new DataNotExistsException("Transaction not found.");
        }

        TransactionEntity tx = transactions.get(0);
        tx.setUserId(newData.getUserId());
        tx.setBookId(newData.getBookId());
        tx.setStatus(newData.getStatus().toLowerCase());
        tx.setReturnDate(LocalDate.parse(newData.getReturnDate()));
        return transactionRepositoryImpl.update(tx);
    }

    public void deleteTransaction(Long id) {
        transactionRepositoryImpl.deleteById(id);
    }
}
