package com.example.umm_library_backend.service;

import com.example.umm_library_backend.dto.user.CreateUserRequest;
import com.example.umm_library_backend.dto.user.UpdateUserRequest;
import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.exception.EmailAlreadyExistsException;
import com.example.umm_library_backend.model.UsersEntity;
import com.example.umm_library_backend.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserRepositoryImpl userRepository;

    public UserServiceImpl(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public List<UsersEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(CreateUserRequest createUserRequest) {
        Optional<UsersEntity> checkEmail = userRepository.findByEmail(createUserRequest.getEmail());
        if (!checkEmail.isEmpty()) {
            throw new EmailAlreadyExistsException("Username already exist.");
        }

        UsersEntity user = new UsersEntity();
        user.setFullName(createUserRequest.getFullName());
        user.setMajor(createUserRequest.getMajor());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRole("user");
        userRepository.save(user);
    }

    public UsersEntity partialUpdateUser(long id, UpdateUserRequest newData) {
        List<UsersEntity> users = userRepository.findId(id);
        if (users == null) {
            throw new DataNotExistsException("User not found.");
        }

        UsersEntity user = users.get(0);
        user.setEmail(newData.getEmail());
        user.setFullName(newData.getFullName());
        user.setMajor(newData.getMajor());
        return userRepository.update(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
