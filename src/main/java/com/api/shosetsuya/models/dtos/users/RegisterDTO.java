package com.api.shosetsuya.models.dtos.users;

import lombok.Getter;

@Getter
public class RegisterDTO {
    private String username;
    private String password;
    private String confirmPassword;
}
