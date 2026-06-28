package org.example.springeshopapi.service;

import org.example.springeshopapi.dto.UserDTO;
import org.example.springeshopapi.dto.UserLoginRequest;
import org.example.springeshopapi.dto.UserRegisterRequest;

public interface UserService {
    UserDTO registerUser(UserRegisterRequest registerRequest);
    String loginUser(UserLoginRequest loginRequest);
}