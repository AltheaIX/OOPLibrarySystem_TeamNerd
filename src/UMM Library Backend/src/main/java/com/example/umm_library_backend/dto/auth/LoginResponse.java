package com.example.umm_library_backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginResponse<T> {
    private int status;
    private String message;
    private T data;

    public LoginResponse() {
    }

    public LoginResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public LoginResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public LoginResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}