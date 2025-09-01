package com.api.shosetsuya.models.dtos.translation_group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateTranslationGroupRequest {

    @NotBlank(message = "{error.field.required}")
    private String name;

    private String avatarUrl;
    private String bio;
}
