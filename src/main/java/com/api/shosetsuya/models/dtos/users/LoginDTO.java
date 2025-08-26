package com.api.shosetsuya.models.dtos.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginDTO {

    @NotBlank(message = "{error.field.required}")
    private String username;

    @NotBlank(message = "{error.field.required}")
    private String password;
}
