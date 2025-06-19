package com.example.umm_library_backend.dto.borrow_book;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RetrieveBorrowByUserResponse<T> {
    private int status;
    private String message;
    private T data;


    public RetrieveBorrowByUserResponse() {
    }

    public RetrieveBorrowByUserResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public RetrieveBorrowByUserResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public RetrieveBorrowByUserResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}