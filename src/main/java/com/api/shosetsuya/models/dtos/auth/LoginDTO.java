package com.api.shosetsuya.models.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginDTO {

    @NotBlank(message = "{error.field.required}")
    @Email(message = "{error.field.invalid}")
    private String email;

    @NotBlank(message = "{error.field.required}")
    private String password;
}
