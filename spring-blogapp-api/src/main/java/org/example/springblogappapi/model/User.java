package org.example.springblogappapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username should not be empty!")
    @Size(min = 4,max = 20,message = "Username length must be between 4 and 20")
    @Column(nullable = false,unique = true)
    private String username;

    @NotBlank(message = "Email must not be empty!")
    @Email(message = "Input must be correct email format")
    @Column(nullable = false,unique = true)
    private String email;
}
