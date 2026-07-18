package org.example.ticketbookingsystem.service;

import lombok.RequiredArgsConstructor;
import org.example.ticketbookingsystem.domain.Role;
import org.example.ticketbookingsystem.domain.User;
import org.example.ticketbookingsystem.dto.AuthResponse;
import org.example.ticketbookingsystem.dto.LoginRequest;
import org.example.ticketbookingsystem.dto.RegisterRequest;
import org.example.ticketbookingsystem.exception.ConflictException;
import org.example.ticketbookingsystem.exception.UnauthorizedException;
import org.example.ticketbookingsystem.repository.UserRepository;
import org.example.ticketbookingsystem.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email is already in use");
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved.getEmail());
        return toAuthResponse(saved, token);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        String token = jwtService.generateToken(user.getEmail());
        return toAuthResponse(user, token);
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
