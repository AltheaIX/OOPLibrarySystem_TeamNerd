package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.model.UsersEntity;

import java.util.Optional;

public interface AuthRepository {
    public Optional<UsersEntity> findByEmail(String email);
    public void save(UsersEntity user);
    public void updatePasswordByEmail(String email, String password);
}
