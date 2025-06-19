package com.example.umm_library_backend.service;

import com.example.umm_library_backend.dto.user.CreateUserRequest;
import com.example.umm_library_backend.dto.user.UpdateUserRequest;
import com.example.umm_library_backend.model.UsersEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UsersEntity> getAllUsers();
    void addUser(CreateUserRequest createUserRequest);
    UsersEntity partialUpdateUser(long id, UpdateUserRequest newData);
    void deleteUser(Long id);
}
