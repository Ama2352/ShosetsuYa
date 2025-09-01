package com.api.shosetsuya.models.dtos.novel;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class NovelTranslationDTO {
    private String novelId;
    private String translationGroupId;
    private String status;
    private String type;
    private String authorName;
    private String artistName;
    private String coverImageUrl;
    private long wordCount;
    private long viewCount;
    private BigDecimal averageRating;
    private String description;
    private String createdAt;
    private String updatedAt;

    public NovelTranslationDTO(
            String novelId, String translationGroupId, String status, String type, String authorName,
            String artistName, String coverImageUrl, String description, String createdAt, String updatedAt
    ) {
        this.novelId = novelId;
        this.translationGroupId = translationGroupId;
        this.status = status;
        this.type = type;
        this.authorName = authorName;
        this.artistName = artistName;
        this.coverImageUrl = coverImageUrl;
        this.wordCount = 0;
        this.viewCount = 0;
        this.averageRating = BigDecimal.ZERO;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
