package com.example.umm_library_backend.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthModel {
    private long id;

    @Column(name = "full_name")
    private String fullName;
    private String role;

    public AuthModel(long id, String fullName, String role) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
    }
}
