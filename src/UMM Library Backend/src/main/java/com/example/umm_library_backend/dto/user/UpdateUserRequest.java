package com.example.umm_library_backend.dto.user;

import lombok.Getter;

@Getter
public class UpdateUserRequest {
    private String fullName;
    private String email;
    private String major;

    public UpdateUserRequest(String fullName, String email, String major) {
        this.fullName = fullName;
        this.email = email;
        this.major = major;
    }
}
