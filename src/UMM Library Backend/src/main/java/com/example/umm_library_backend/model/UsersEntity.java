package com.example.umm_library_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name")
    private String fullName;

    private String major;
    private String email;
    private String password;
    private String role;

    public UsersEntity(){

    }

    public UsersEntity(String fullName, String major, String email, String password, String role) {
        this.fullName = fullName;
        this.major = major;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
