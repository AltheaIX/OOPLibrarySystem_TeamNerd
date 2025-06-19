package com.example.umm_library_backend.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String fullName;
    private String email;
    private String password;
    private String major;
}
