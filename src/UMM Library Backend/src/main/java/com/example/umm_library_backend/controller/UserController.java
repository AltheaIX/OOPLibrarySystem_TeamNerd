package com.example.umm_library_backend.controller;

import com.example.umm_library_backend.dto.auth.RegisterResponse;
import com.example.umm_library_backend.dto.user.*;
import com.example.umm_library_backend.model.UsersEntity;
import com.example.umm_library_backend.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public ResponseEntity<RetrieveUserResponse> getAll(){
        List<UsersEntity> usersList = userServiceImpl.getAllUsers();
        return ResponseEntity.ok(new RetrieveUserResponse(200, usersList));
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> addUser(@RequestBody(required = false) CreateUserRequest request){
        if(request == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CreateUserResponse<>(400, "Bad request"));
        }

        userServiceImpl.addUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(new CreateUserResponse<>(200, "User added"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest user){
        UsersEntity updatedUser = userServiceImpl.partialUpdateUser(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateUserResponse<>(200, updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable Long id){
        userServiceImpl.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteUserResponse<>(200, true));
    }
}
