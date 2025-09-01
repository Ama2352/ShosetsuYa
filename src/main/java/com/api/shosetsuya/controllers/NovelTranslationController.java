package com.api.shosetsuya.controllers;

import com.api.shosetsuya.helpers.ApiResponse;
import com.api.shosetsuya.models.dtos.novel.AddNovelCategoriesRequest;
import com.api.shosetsuya.models.dtos.novel.AddNovelTitlesRequest;
import com.api.shosetsuya.models.dtos.novel.AddNovelTranslationCoreRequest;
import com.api.shosetsuya.models.dtos.novel.FullNovelTranslationDetails;
import com.api.shosetsuya.services.NovelTranslationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerToken")
@RequestMapping("/novel-translations")
@Tag(name = "Novel Translation")
@PreAuthorize("hasRole('USER')")
public class NovelTranslationController {

    private final NovelTranslationService novelTranslationService;
    private final MessageSource messageSource;

    @PostMapping("/add-titles")
    @Operation(
            summary = "Add titles to a novel translation",
            description = "Adds multiple titles to a specified novel translation. Requires exactly one main title."
    )
    @PreAuthorize("hasAuthority('TG_TRANSLATOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addNovelTitles(@RequestBody @Valid AddNovelTitlesRequest request) {
        novelTranslationService.addNovelTitles(request);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                messageSource.getMessage("data.added.successfully", null, LocaleContextHolder.getLocale()),
                null
            )
        );
    }

    @PostMapping("/add-categories")
    @Operation(
            summary = "Add categories to a novel translation",
            description = "Associates multiple categories with a specified novel translation."
    )
    @PreAuthorize("hasAuthority('TG_TRANSLATOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addNovelCategories(@RequestBody @Valid AddNovelCategoriesRequest request) {
        novelTranslationService.addNovelCategories(request);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                messageSource.getMessage("data.added.successfully", null, LocaleContextHolder.getLocale()),
                null
            )
        );
    }

    @PostMapping("/add-details")
    @Operation(
            summary = "Add core details to a novel translation",
            description = "Adds core details such as type, status, and associated translation group to a novel translation."
    )
    @PreAuthorize("hasAuthority('TG_TRANSLATOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addNovelDetails(@RequestBody @Valid AddNovelTranslationCoreRequest request) {
        novelTranslationService.addNovelTranslationCore(request);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                messageSource.getMessage("data.added.successfully", null, LocaleContextHolder.getLocale()),
                null
            )
        );
    }

    @GetMapping("/full-details/{novelId}")
    @Operation(
            summary = "Get full details of a novel translation",
            description = "Retrieves comprehensive details of a novel translation including titles, categories, and core information."
    )
    public ResponseEntity<ApiResponse<?>> getFullNovelDetails(@PathVariable("novelId") UUID novelId) {
        FullNovelTranslationDetails details = novelTranslationService.getFullNovelTranslationDetails(novelId);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                messageSource.getMessage("data.fetch.successfully", null, LocaleContextHolder.getLocale()),
                details
            )
        );
    }
}
