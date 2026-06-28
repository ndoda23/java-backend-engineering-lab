package org.example.springeshopapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springeshopapi.dto.UserDTO;
import org.example.springeshopapi.dto.UserLoginRequest;
import org.example.springeshopapi.dto.UserRegisterRequest;
import org.example.springeshopapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRegisterRequest registerRequest) {
        UserDTO createdUser = userService.registerUser(registerRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest);
        return ResponseEntity.ok(token);
    }
}