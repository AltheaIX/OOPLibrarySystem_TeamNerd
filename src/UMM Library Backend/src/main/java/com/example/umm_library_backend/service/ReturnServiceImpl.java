package com.example.umm_library_backend.service;

import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.model.BooksEntity;
import com.example.umm_library_backend.model.TransactionEntity;
import com.example.umm_library_backend.repository.BookRepositoryImpl;
import com.example.umm_library_backend.repository.TransactionRepositoryImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReturnServiceImpl implements ReturnService {
    private final TransactionRepositoryImpl transactionRepository;
    private final BookRepositoryImpl bookRepository;

    public ReturnServiceImpl(TransactionRepositoryImpl transactionRepository, BookRepositoryImpl bookRepository) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
    }

    public long dayPassed(LocalDate returnDate) {
        LocalDate now = LocalDate.now();
        if(!now.isAfter(returnDate)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(returnDate, now);
    }

    public long calculateFee(long dayPassed){
        return dayPassed * 500;
    }

    @Transactional
    public void returnBook(Long transactionId){
        List<TransactionEntity> transactions = transactionRepository.findById(transactionId);
        TransactionEntity tx = transactions.get(0);

        if (tx == null) {
            throw new DataNotExistsException("Transaction not found");
        }

        List<BooksEntity> booksEntity = bookRepository.findById(tx.getBookId());
        if (booksEntity == null || booksEntity.size() == 0) {
            throw new DataNotExistsException("Book not found");
        }

        BooksEntity book = booksEntity.get(0);
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.update(book);

        tx.setStatus("returned");
        transactionRepository.update(tx);
    }
}
