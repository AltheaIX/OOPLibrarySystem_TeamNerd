package com.example.umm_library_backend.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class AuthModel {
    private long id;

    @Column(name = "full_name")
    private String fullName;
    private String role;
    private String email;
    private Timestamp createdAt;

    public AuthModel(long id, String email, String fullName, String role, Timestamp createdAt) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
    }
}
