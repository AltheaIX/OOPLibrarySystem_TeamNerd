package com.example.umm_library_backend.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String fullName;
    private String major;
    private String email;
    private String password;

    public RegisterRequest(String fullName, String major, String email, String password) {
        this.fullName = fullName;
        this.major = major;
        this.email = email;
        this.password = password;
    }
}
