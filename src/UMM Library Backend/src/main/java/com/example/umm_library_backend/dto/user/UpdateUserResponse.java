package com.example.umm_library_backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UpdateUserResponse<T> {
    private int status;
    private String message;
    private T data;


    public UpdateUserResponse() {
    }

    public UpdateUserResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public UpdateUserResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public UpdateUserResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}