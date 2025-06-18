package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.model.BooksEntity;

import java.util.List;

public interface BookRepository {
    public void save(BooksEntity book);
    public List<BooksEntity> findAll();
    public List<BooksEntity> findById(long id);
    public List<BooksEntity> findByIsbn(String isbn);
    public List<BooksEntity> findByName(String name);
    public List<BooksEntity> findByAuthor(String author);
    public BooksEntity update(BooksEntity book);
    public void deleteById(Long id);
}
