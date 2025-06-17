package com.example.umm_library_backend.dto.borrow_book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowBookRequest {
    private long userId;
    private long bookId;

    public BorrowBookRequest(long userId, long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }
}
