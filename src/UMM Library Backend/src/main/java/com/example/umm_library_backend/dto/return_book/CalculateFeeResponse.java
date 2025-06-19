package com.example.umm_library_backend.dto.return_book;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CalculateFeeResponse<T> {
    private int status;
    private String message;
    private T data;


    public CalculateFeeResponse() {
    }

    public CalculateFeeResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public CalculateFeeResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public CalculateFeeResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}
