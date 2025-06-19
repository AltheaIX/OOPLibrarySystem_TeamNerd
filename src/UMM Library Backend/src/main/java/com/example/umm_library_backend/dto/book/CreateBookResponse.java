package com.example.umm_library_backend.dto.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateBookResponse<T> {
    private int status;
    private String message;
    private T data;


    public CreateBookResponse() {
    }

    public CreateBookResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public CreateBookResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public CreateBookResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}