package com.example.umm_library_backend.dto.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DeleteBookResponse<T> {
    private int status;
    private String message;
    private T data;


    public DeleteBookResponse() {
    }

    public DeleteBookResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public DeleteBookResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public DeleteBookResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}