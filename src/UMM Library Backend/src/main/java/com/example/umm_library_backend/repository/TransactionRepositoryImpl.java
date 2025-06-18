package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.model.BooksEntity;
import com.example.umm_library_backend.model.TransactionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl {
    @PersistenceContext
    private EntityManager em;

    public void save(TransactionEntity transaction) {
        em.persist(transaction);
    }

    public TransactionEntity update(TransactionEntity transaction) {
        return em.merge(transaction);
    }

    public TransactionEntity findById(long id) {
        return em.find(TransactionEntity.class, id);
    }
}
