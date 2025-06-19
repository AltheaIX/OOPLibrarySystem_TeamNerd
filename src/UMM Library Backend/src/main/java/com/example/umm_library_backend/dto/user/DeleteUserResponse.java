package com.example.umm_library_backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DeleteUserResponse<T> {
    private int status;
    private String message;
    private T data;


    public DeleteUserResponse() {
    }

    public DeleteUserResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public DeleteUserResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public DeleteUserResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}