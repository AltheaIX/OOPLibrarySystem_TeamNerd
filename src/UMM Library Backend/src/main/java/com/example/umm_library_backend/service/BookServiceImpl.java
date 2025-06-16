package com.example.umm_library_backend.service;

import com.example.umm_library_backend.dto.book.CreateBookRequest;
import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.model.BooksEntity;
import com.example.umm_library_backend.repository.BookRepository;
import com.example.umm_library_backend.repository.BookRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BooksEntity addBook(CreateBookRequest book) {
        BooksEntity bookEntity = new BooksEntity();
        bookEntity.setIsbn(book.getIsbn());
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setTitle(book.getTitle());
        bookEntity.setQuantity(book.getQuantity());
        bookRepository.save(bookEntity);
        return bookEntity;
    }

    public List<BooksEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public BooksEntity getBookById(long id) {
        BooksEntity book = bookRepository.findById(id);
        if (book == null) {
            throw new DataNotExistsException("Book not found.");
        }

        return bookRepository.findById(id);
    }

    public BooksEntity updateBook(Long id, BooksEntity data) {
        BooksEntity book = bookRepository.findById(id);
        if (book == null) {
            throw new DataNotExistsException("Book not found.");
        }

        book.setId(id);
        book.setIsbn(data.getIsbn());
        book.setTitle(data.getTitle());
        book.setAuthor(data.getAuthor());
        book.setQuantity(data.getQuantity());

        return bookRepository.update(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
