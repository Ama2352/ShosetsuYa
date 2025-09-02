package com.api.shosetsuya.controllers;

import com.api.shosetsuya.helpers.ApiResponse;
import com.api.shosetsuya.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<?>> getCurrentUserRoles() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        messageSource.getMessage(
                                "data.fetch.success",
                                null,
                                LocaleContextHolder.getLocale()
                        ),
                        userService.getCurrentUserRoles()
                )
        );
    }
}
