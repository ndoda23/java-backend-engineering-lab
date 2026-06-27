package org.example.springeshopapi.dto;


import lombok.Data;
import org.example.springeshopapi.model.Role;

@Data
public class UserDTO {
    private  Long id;
    private String email;
    private String fullName;
    private Role role;
}
