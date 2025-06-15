package com.example.umm_library_backend.controller;

import com.example.umm_library_backend.dto.auth.*;
import com.example.umm_library_backend.model.AuthModel;
import com.example.umm_library_backend.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JavaMailSender mailSender;

    private final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody(required = false) LoginRequest loginRequest) {
        if(loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse<>(400, "Bad request"));
        }

        boolean checkLogin = authServiceImpl.Login(loginRequest);
        if(!checkLogin) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse<>(401, "Invalid email or password"));
        }

        AuthModel data = new AuthModel("Test_access_token");
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse<>(200, data));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody(required = false) RegisterRequest registerRequest) {
        if(registerRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse<>(400, "Bad request"));
        }

        authServiceImpl.Register(registerRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new RegisterResponse<>(200, "Hello"));
    }

    @GetMapping("/forgot")
    public ResponseEntity<ForgotResponse> forgotPassword(@ModelAttribute ForgotRequest forgotRequest) {
        if(forgotRequest == null || forgotRequest.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ForgotResponse<>(400, "Bad request"));
        }

        String newPassword = authServiceImpl.ForgotPassword(forgotRequest);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("althea.ixy@gmail.com");
        message.setTo(forgotRequest.getEmail());
        message.setSubject("Forgot Password");
        message.setText("New generated password: " + newPassword);

        mailSender.send(message);

        return ResponseEntity.status(HttpStatus.OK).body(new ForgotResponse<>(200, "OK"));
    }
}
