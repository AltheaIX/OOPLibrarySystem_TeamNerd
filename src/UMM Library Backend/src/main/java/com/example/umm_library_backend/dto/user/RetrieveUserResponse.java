package com.example.umm_library_backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RetrieveUserResponse<T> {
    private int status;
    private String message;
    private T data;


    public RetrieveUserResponse() {
    }

    public RetrieveUserResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public RetrieveUserResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public RetrieveUserResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}