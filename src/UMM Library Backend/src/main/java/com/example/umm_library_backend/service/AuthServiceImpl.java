package com.example.umm_library_backend.service;

import com.example.umm_library_backend.dto.auth.ChangePasswordRequest;
import com.example.umm_library_backend.dto.auth.ForgotRequest;
import com.example.umm_library_backend.dto.auth.LoginRequest;
import com.example.umm_library_backend.dto.auth.RegisterRequest;
import com.example.umm_library_backend.exception.DataNotExistsException;
import com.example.umm_library_backend.exception.EmailAlreadyExistsException;
import com.example.umm_library_backend.exception.UnauthorizedException;
import com.example.umm_library_backend.model.UsersEntity;
import com.example.umm_library_backend.repository.AuthRepositoryImpl;
import com.example.umm_library_backend.shared.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AuthRepositoryImpl authRepositoryImpl;

    public AuthServiceImpl(AuthRepositoryImpl authRepositoryImpl) {
        this.authRepositoryImpl = authRepositoryImpl;
    }

    public UsersEntity Login(LoginRequest loginRequest) {
        Optional<UsersEntity> checkLogin = authRepositoryImpl.findByEmail(loginRequest.getEmail());
        if (checkLogin.isEmpty()) {
            return null;
        }

        UsersEntity user = checkLogin.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return null;
        }

        return user;
    }

    public void Register(RegisterRequest registerRequest) {
       Optional<UsersEntity> checkEmail = authRepositoryImpl.findByEmail(registerRequest.getEmail());
       if (!checkEmail.isEmpty()) {
           throw new EmailAlreadyExistsException("Username already exist.");
       }

       UsersEntity user = new UsersEntity();
       user.setFullName(registerRequest.getFullName());
       user.setMajor(registerRequest.getMajor());
       user.setEmail(registerRequest.getEmail());
       user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
       user.setRole("user");
       authRepositoryImpl.save(user);
    }

    public String ForgotPassword(ForgotRequest forgotRequest) {
        String randomPassword = PasswordUtil.generateRandomPassword();
        String hashedPassword = passwordEncoder.encode(randomPassword);

        authRepositoryImpl.updatePasswordByEmail(forgotRequest.getEmail(), hashedPassword);
        return randomPassword;
    }

    public void ChangePassword(ChangePasswordRequest changePasswordRequest) {
        Optional<UsersEntity> checkEmail = authRepositoryImpl.findByEmail(changePasswordRequest.getEmail());
        if (checkEmail.isEmpty()) {
            throw new DataNotExistsException("Email not found.");
        }

        UsersEntity user = checkEmail.get();
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Wrong password.");
        }

        String hashedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());

        authRepositoryImpl.updatePasswordByEmail(changePasswordRequest.getEmail(), hashedPassword);
    }

}
