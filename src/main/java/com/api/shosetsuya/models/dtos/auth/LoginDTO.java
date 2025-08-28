package com.api.shosetsuya.models.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginDTO {

    @NotBlank(message = "{error.field.required}")
    @Email(message = "{error.field.invalid}")
    @Schema(example = "user2@gmail.com")
    private String email;

    @NotBlank(message = "{error.field.required}")
    @Schema(example = "user2")
    private String password;
}
