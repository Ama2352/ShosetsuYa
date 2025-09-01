package com.api.shosetsuya.models.dtos.novel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class AddNovelCategoriesRequest {

    @NotNull(message = "{error.field.required}")
    private UUID novelId;

    @NotEmpty(message = "{error.field.required}")
    private List<UUID> categoryIds;
}
