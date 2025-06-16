package com.example.umm_library_backend.dto.book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequest {
    private String isbn;
    private String title;
    private String author;
    private long quantity;

    public CreateBookRequest(String isbn, String title, String author, long quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }
}
