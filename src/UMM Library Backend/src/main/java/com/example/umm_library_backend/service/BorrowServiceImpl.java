package com.example.umm_library_backend.service;

import com.example.umm_library_backend.dto.book.CreateBookRequest;
import com.example.umm_library_backend.dto.borrow_book.BorrowBookRequest;
import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.model.BooksEntity;
import com.example.umm_library_backend.model.TransactionEntity;
import com.example.umm_library_backend.model.UsersEntity;
import com.example.umm_library_backend.repository.BookRepositoryImpl;
import com.example.umm_library_backend.repository.TransactionRepositoryImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowServiceImpl {
    private final TransactionRepositoryImpl transactionRepository;
    private final BookRepositoryImpl bookRepository;

    public BorrowServiceImpl(TransactionRepositoryImpl transactionRepository, BookRepositoryImpl bookRepository) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public TransactionEntity borrowBook(BorrowBookRequest borrowBookRequest) {
        List<BooksEntity> booksEntity = bookRepository.findById(borrowBookRequest.getBookId());
        if (booksEntity == null || booksEntity.isEmpty()) {
            throw new DataNotExistsException("Book not found");
        }

        BooksEntity book = booksEntity.get(0);
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.update(book);

        LocalDate returnDate = LocalDate.now().plusDays(7);
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setBookId(borrowBookRequest.getBookId());
        transactionEntity.setUserId(borrowBookRequest.getUserId());
        transactionEntity.setReturnDate(returnDate);
        transactionEntity.setStatus("borrowed");
        transactionRepository.save(transactionEntity);

        return transactionEntity;
    }
}
