package com.api.shosetsuya.models.dtos.novel;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class NovelTitleDTO {
    String titleId;
    String title;
    String language;
    boolean isMainTitle;
}
