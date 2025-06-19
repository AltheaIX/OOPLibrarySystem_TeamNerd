package com.example.umm_library_backend.repository;


import com.example.umm_library_backend.model.TransactionEntity;

import java.util.List;

public interface TransactionRepository {
    void save(TransactionEntity transaction);
    TransactionEntity update(TransactionEntity transaction);
    List<TransactionEntity> findAll();
    List<TransactionEntity> findById(long id);
    List<TransactionEntity> findBorrowedByUserId(long userId);
    List<TransactionEntity> findBorrowedByUserAndBook(Long userId, Long bookId);
    void deleteById(Long id);
}
