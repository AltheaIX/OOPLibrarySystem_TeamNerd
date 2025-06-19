package com.example.umm_library_backend.repository;

import com.example.umm_library_backend.model.UsersEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<UsersEntity> findAll();
    List<UsersEntity> findId(long id);
    void save(UsersEntity user);
    UsersEntity update(UsersEntity user);
    Optional<UsersEntity> findByEmail(String email);
    void deleteById(Long id);
}