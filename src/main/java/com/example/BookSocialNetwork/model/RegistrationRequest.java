package com.example.BookSocialNetwork.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "FirstName is mandatory")
    @NotBlank(message = "FirstName is mandatory")
    private String firstName;

    @NotEmpty(message = "LastName is mandatory")
    @NotBlank(message = "LastName is mandatory")
    private String lastName;

    @Email(message = "Email is not formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Size(min = 0,message = "Password should be 8 characters long minimum")
    @NotEmpty(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    private String password;



}
