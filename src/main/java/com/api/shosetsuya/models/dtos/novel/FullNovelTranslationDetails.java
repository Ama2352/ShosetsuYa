package com.api.shosetsuya.models.dtos.novel;

import com.api.shosetsuya.models.dtos.catogry.CategoryDTO;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class FullNovelTranslationDetails {
    NovelTranslationDTO novelDetails;
    List<NovelTitleDTO> novelTitles;
    List<CategoryDTO> novelCategories;
}
