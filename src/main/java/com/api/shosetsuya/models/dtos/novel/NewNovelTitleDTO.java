package com.api.shosetsuya.models.dtos.novel;

import lombok.Getter;

import java.util.UUID;

@Getter
public class NewNovelTitleDTO {
    private UUID novelId;
    private String title;
    private boolean isMainTitle;
    private String language;
}
