package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.model.BooksEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
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

    public List<BooksEntity> findById(long id) {
        BooksEntity book = em.find(BooksEntity.class, id);
        if (book == null) {
            return null;
        }

        List<BooksEntity> list = new ArrayList<>();
        list.add(book);

        return list;
    }

    public List<BooksEntity> findByIsbn(String isbn) {
        List<BooksEntity> result = em.createQuery("SELECT b FROM BooksEntity b WHERE b.isbn = :isbn", BooksEntity.class)
                .setParameter("isbn", isbn)
                .getResultList();
        return result.isEmpty() ? null : result;
    }

    public List<BooksEntity> findByName(String name) {
        List<BooksEntity> result = em.createQuery("SELECT b FROM BooksEntity b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :name, '%'))", BooksEntity.class)
                .setParameter("name", name)
                .getResultList();
        return result.isEmpty() ? null : result;
    }

    public List<BooksEntity> findByAuthor(String author) {
        List<BooksEntity> result = em.createQuery("SELECT b FROM BooksEntity b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))", BooksEntity.class)
                .setParameter("author", author)
                .getResultList();
        return result.isEmpty() ? null : result;
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
