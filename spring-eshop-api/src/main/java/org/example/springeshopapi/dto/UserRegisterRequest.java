package org.example.springeshopapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {
   @NotBlank(message = "Email is required")
   @Email(message = "Please provide a valid email address")
   private String email;

   @NotBlank(message = "Password cannot be empty")
   @Size(min = 6, message = "Password must be at least 6 characters long")
   private String password;

   @NotBlank(message = "Name cannot be empty")
   private String fullName;

}
