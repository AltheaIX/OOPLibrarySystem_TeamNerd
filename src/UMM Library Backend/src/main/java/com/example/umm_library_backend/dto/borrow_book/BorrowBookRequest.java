package com.example.umm_library_backend.dto.borrow_book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowBookRequest {
    private long userId;
    private long bookId;
    private long days;

    public BorrowBookRequest(long userId, long bookId, long days) {
        this.userId = userId;
        this.bookId = bookId;
        this.days = days;
    }
}
