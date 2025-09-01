package com.api.shosetsuya.services;

import com.api.shosetsuya.helpers.exceptions.ResourceNotFoundException;
import com.api.shosetsuya.helpers.utils.DateTimeUtils;
import com.api.shosetsuya.models.dtos.catogry.CategoryDTO;
import com.api.shosetsuya.models.dtos.novel.*;
import com.api.shosetsuya.models.entities.Category;
import com.api.shosetsuya.models.entities.NovelTitle;
import com.api.shosetsuya.models.entities.NovelTranslation;
import com.api.shosetsuya.models.entities.TranslationGroup;
import com.api.shosetsuya.models.enums.NovelStatus;
import com.api.shosetsuya.models.enums.NovelType;
import com.api.shosetsuya.repositories.CategoryRepo;
import com.api.shosetsuya.repositories.NovelTranslationRepo;
import com.api.shosetsuya.repositories.TranslationGroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NovelTranslationService {

    private final NovelTranslationRepo novelTranslationRepo;
    private final MessageSource messageSource;
    private final CategoryRepo categoryRepo;
    private final TranslationGroupService translationGroupService;

    public NovelTranslation getNovelTranslationById(UUID novelId) {
        return novelTranslationRepo.findById(novelId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.novel.not-found", null, LocaleContextHolder.getLocale())
                ));
    }

    @Transactional
    public void addNovelTitles(AddNovelTitlesRequest request) {
        NovelTranslation novel = getNovelTranslationById(request.getNovelId());

        long mainTitleCount = request.getTitles().stream()
                .filter(AddNovelTitlesRequest.TitleRequest::isMainTitle)
                .count();

        boolean hasMainTitle = novel.getTitles().stream()
                .anyMatch(NovelTitle::isMainTitle);

        if((hasMainTitle && mainTitleCount != 0) || (!hasMainTitle && mainTitleCount != 1)) {
            throw new IllegalArgumentException(messageSource.getMessage("error.novel.title.main-required", null, LocaleContextHolder.getLocale()));
        }

        Set<NovelTitle> titles = request.getTitles().stream()
                .map(titleRequest -> {
                    NovelTitle title = new NovelTitle(
                        titleRequest.getTitle(),
                        titleRequest.getLanguage(),
                        titleRequest.isMainTitle()
                    );
                    title.setNovel(novel);
                    return title;
                })
                .collect(Collectors.toSet());

        novel.getTitles().addAll(titles);
        novelTranslationRepo.save(novel);
    }

    @Transactional
    public void addNovelCategories(AddNovelCategoriesRequest request) {
        NovelTranslation novel = getNovelTranslationById(request.getNovelId());

        Set<Category> categories = new HashSet<>(categoryRepo.findAllById(request.getCategoryIds()));
        if(categories.size() != request.getCategoryIds().size()) {
            throw new ResourceNotFoundException(messageSource.getMessage("error.category.ids.not-found", null, LocaleContextHolder.getLocale()));
        }

        novel.getCategories().clear();

        novel.getCategories().addAll(categories);
        categories.forEach(category -> category.getNovels().add(novel));

        novelTranslationRepo.save(novel);
        categoryRepo.saveAll(categories); // bi-directional
    }

    @Transactional
    public void addNovelTranslationCore(AddNovelTranslationCoreRequest request) {
        TranslationGroup group = translationGroupService.getTranslationGroupById(request.getTranslationGroupId());

        NovelTranslation novel = new NovelTranslation();
        novel.setTranslationGroup(group);
        novel.setAuthorName(request.getAuthorName());
        novel.setArtistName(request.getArtistName());
        novel.setCoverImageUrl(request.getCoverImageUrl());
        novel.setDescription(request.getDescription());
        novel.setStatus(NovelStatus.valueOf(request.getStatus().toUpperCase()));
        novel.setType(NovelType.valueOf(request.getType().toUpperCase()));

        novelTranslationRepo.save(novel);
    }

    public FullNovelTranslationDetails getFullNovelTranslationDetails(UUID novelId) {
        NovelTranslation novel = getNovelTranslationById(novelId);

        List<NovelTitleDTO> titles = novel.getTitles().stream()
                .map(title -> new NovelTitleDTO(
                        title.getTitleId().toString(),
                        title.getTitle(),
                        title.getLanguage(),
                        title.isMainTitle()
                ))
                .collect(Collectors.toList());

        List<CategoryDTO> categories = novel.getCategories().stream()
                .map(category -> new CategoryDTO(
                        category.getCategoryId().toString(),
                        category.getName()
                ))
                .collect(Collectors.toList());

        NovelTranslationDTO novelDetails = new NovelTranslationDTO(
                novel.getNovelId().toString(),
                novel.getTranslationGroup().getGroupId().toString(),
                novel.getStatus().toString(),
                novel.getType().toString(),
                novel.getAuthorName(),
                novel.getArtistName(),
                novel.getCoverImageUrl(),
                novel.getDescription(),
                DateTimeUtils.format(novel.getCreatedAt()),
                DateTimeUtils.format(novel.getUpdatedAt())
        );

        return new FullNovelTranslationDetails(
                novelDetails,
                titles,
                categories
        );
    }
}
