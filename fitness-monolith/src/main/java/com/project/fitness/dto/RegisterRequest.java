package com.project.fitness.dto;

import com.project.fitness.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Email is required !")
    @Email(message =  "invalid email !")
    private String email ;
    @NotBlank(message = "Password is necessary ! ")
    private  String password;
    private String firstName ;
    private  String lastName;
    private UserRole role;
}
