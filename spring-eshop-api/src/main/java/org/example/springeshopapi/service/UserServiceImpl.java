package org.example.springeshopapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springeshopapi.dto.UserDTO;
import org.example.springeshopapi.dto.UserLoginRequest;
import org.example.springeshopapi.dto.UserRegisterRequest;
import org.example.springeshopapi.model.Role;
import org.example.springeshopapi.model.User;
import org.example.springeshopapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserDTO registerUser(UserRegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setRole(Role.ROLE_CUSTOMER);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setFullName(savedUser.getFullName());
        userDTO.setEmail(savedUser.getEmail());

        return userDTO;


    }

    @Override
    public String loginUser(UserLoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }
        return jwtService.generateToken(user.getEmail());
    }
}
