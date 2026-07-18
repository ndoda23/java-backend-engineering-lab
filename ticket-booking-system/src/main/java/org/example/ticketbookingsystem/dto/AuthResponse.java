package org.example.ticketbookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ticketbookingsystem.domain.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
