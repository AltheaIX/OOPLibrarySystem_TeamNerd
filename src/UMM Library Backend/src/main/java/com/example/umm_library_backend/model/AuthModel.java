package com.example.umm_library_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthModel {
    private String accessToken;

    public AuthModel(String accessToken) {
        this.accessToken = accessToken;
    }
}
