package dto;

import model.User;

public class LoginResponse {
    public int status;
    public String message; // nullable
    public User data;  // nullable
}