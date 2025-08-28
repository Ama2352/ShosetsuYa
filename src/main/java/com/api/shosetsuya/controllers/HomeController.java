package com.api.shosetsuya.controllers;

import com.api.shosetsuya.helpers.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@Tag(name = "Home")
public class HomeController {

    @SecurityRequirement(name = "bearerToken")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> home() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Welcome to ShosetsuYa API", null));
    }
}
