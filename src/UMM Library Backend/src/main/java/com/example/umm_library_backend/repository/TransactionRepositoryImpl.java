package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.model.BooksEntity;
import com.example.umm_library_backend.model.TransactionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class TransactionRepositoryImpl implements TransactionRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(TransactionEntity transaction) {
        em.persist(transaction);
    }

    public TransactionEntity update(TransactionEntity transaction) {
        return em.merge(transaction);
    }

    public List<TransactionEntity> findAll() {
        return em.createQuery("SELECT t from TransactionEntity t", TransactionEntity.class).getResultList();
    }

    public List<TransactionEntity> findById(long id) {
        TransactionEntity tx = em.find(TransactionEntity.class, id);
        if (tx == null) {
            return null;
        }

        List<TransactionEntity> transactions = new ArrayList<>();
        transactions.add(tx);
        return transactions;
    }

    public List<TransactionEntity> findBorrowedByUserId(long userId) {
        return em.createQuery(
                        "SELECT t FROM TransactionEntity t WHERE t.userId = :userId AND t.status = 'borrowed'",
                        TransactionEntity.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<TransactionEntity> findBorrowedByUserAndBook(Long userId, Long bookId) {
        return em.createQuery(
                        "SELECT t FROM TransactionEntity t WHERE t.userId = :userId AND t.bookId = :bookId AND t.status = 'borrowed'",
                        TransactionEntity.class
                )
                .setParameter("userId", userId)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    public void deleteById(Long id) {
        TransactionEntity tx = em.find(TransactionEntity.class, id);
        if (tx != null) {
            em.remove(tx);
            return;
        }

        throw new DataNotExistsException("Transaction not found");
    }
}
