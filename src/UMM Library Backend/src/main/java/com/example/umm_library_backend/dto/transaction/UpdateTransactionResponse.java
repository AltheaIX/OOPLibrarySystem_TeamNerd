package com.example.umm_library_backend.dto.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UpdateTransactionResponse<T> {
    private int status;
    private String message;
    private T data;


    public UpdateTransactionResponse() {
    }

    public UpdateTransactionResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public UpdateTransactionResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public UpdateTransactionResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}