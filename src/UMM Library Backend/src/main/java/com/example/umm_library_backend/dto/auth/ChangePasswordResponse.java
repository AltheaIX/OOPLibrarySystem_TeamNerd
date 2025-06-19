package com.example.umm_library_backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChangePasswordResponse<T> {
    private int status;
    private String message;
    private T data;

    public ChangePasswordResponse() {
    }

    public ChangePasswordResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ChangePasswordResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ChangePasswordResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}
