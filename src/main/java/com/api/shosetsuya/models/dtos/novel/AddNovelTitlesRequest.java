package com.api.shosetsuya.models.dtos.novel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class AddNovelTitlesRequest {

    @NotNull(message = "{error.field.required}")
    private UUID novelId;

    @NotEmpty(message = "{error.field.required}")
    private List<TitleRequest> titles;

    @Getter
    public static class TitleRequest {

        @NotBlank(message = "{error.field.required}")
        private String title;

        @NotBlank(message = "{error.field.required}")
        private String language;

        private boolean isMainTitle;
    }

}

