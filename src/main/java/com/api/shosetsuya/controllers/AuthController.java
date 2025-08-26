package com.api.shosetsuya.controllers;

import com.api.shosetsuya.models.dtos.users.LoginDTO;
import com.api.shosetsuya.models.dtos.users.RegisterDTO;
import com.api.shosetsuya.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody RegisterDTO dto) {
        boolean success = userService.register(dto);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(token);
    }

}
