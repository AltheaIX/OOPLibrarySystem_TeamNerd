package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.model.BooksEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class BookRepositoryImpl implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(BooksEntity book) {
        em.persist(book);
    }

    public BooksEntity findById(Long id) {
        return em.find(BooksEntity.class, id);
    }

    public List<BooksEntity> findAll() {
        return em.createQuery("SELECT b FROM BooksEntity b", BooksEntity.class).getResultList();
    }

    public BooksEntity findById(long id) {
        return em.find(BooksEntity.class, id);
    }

    public BooksEntity update(BooksEntity book) {
        return em.merge(book);
    }

    public void deleteById(Long id) {
        BooksEntity book = em.find(BooksEntity.class, id);
        if (book != null) {
            em.remove(book);
            return;
        }

        throw new DataNotExistsException("Book not found");
    }
}
