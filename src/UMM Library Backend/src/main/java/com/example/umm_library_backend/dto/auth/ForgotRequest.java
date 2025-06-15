package com.example.umm_library_backend.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotRequest {
    private String email;
    public ForgotRequest(String email) {
        this.email = email;
    }
}
