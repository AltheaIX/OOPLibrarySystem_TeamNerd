package com.example.umm_library_backend.service;

import com.example.umm_library_backend.dto.borrow_book.BorrowBookRequest;
import com.example.umm_library_backend.model.TransactionEntity;

import java.util.List;

public interface BorrowService {
    TransactionEntity borrowBook(BorrowBookRequest borrowBookRequest);
    List<TransactionEntity> getBorrowedBooksByUserId(long userId);
    boolean isUserAlreadyBorrowBook(long userId, long bookId);
}
