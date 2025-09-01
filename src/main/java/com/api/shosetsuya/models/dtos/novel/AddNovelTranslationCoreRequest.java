package com.api.shosetsuya.models.dtos.novel;

import com.api.shosetsuya.helpers.EnumValidator;
import com.api.shosetsuya.models.enums.NovelStatus;
import com.api.shosetsuya.models.enums.NovelType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.UUID;

@Getter
public class AddNovelTranslationCoreRequest {

    @NotBlank(message = "{error.field.required}")
    private UUID translationGroupId;

    @NotBlank(message = "{error.field.required}")
    @EnumValidator(enumClass = NovelStatus.class, message = "{error.field.invalid}")
    private String status;

    @NotBlank(message = "{error.field.required}")
    @EnumValidator(enumClass = NovelType.class, message = "{error.field.invalid}")
    private String type;

    @NotBlank(message = "{error.field.required}")
    private String authorName;

    private String coverImageUrl;
    private String description;
    private String artistName;
}
