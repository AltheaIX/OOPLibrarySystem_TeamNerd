package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.model.BooksEntity;

import java.util.List;

public interface BookRepository {
    public void save(BooksEntity book);
    public List<BooksEntity> findAll();
    public BooksEntity findById(long id);
    public BooksEntity update(BooksEntity book);
    public void deleteById(Long id);
}
