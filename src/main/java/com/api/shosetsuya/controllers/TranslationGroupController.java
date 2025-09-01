package com.api.shosetsuya.controllers;

import com.api.shosetsuya.helpers.ApiResponse;
import com.api.shosetsuya.models.dtos.translation_group.CreateTranslationGroupRequest;
import com.api.shosetsuya.services.TranslationGroupService;
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

@RestController
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
@RequestMapping("/translation-groups")
@Tag(name = "Translation Group")
@PreAuthorize("hasRole('USER')")
public class TranslationGroupController {

    private final TranslationGroupService translationGroupService;
    private final MessageSource messageSource;

    @GetMapping("/get-all")
    @Operation(summary = "Get all translation groups")
    public ResponseEntity<ApiResponse<?>> getAllTranslationGroups() {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                messageSource.getMessage(
                        "data.fetch.success",
                        null,
                        LocaleContextHolder.getLocale()
                ),
                translationGroupService.getAllTranslationGroups()
            )
        );
    }

    @PostMapping("/create-group")
    @Operation(summary = "Create a new translation group")
    public ResponseEntity<ApiResponse<?>> createTranslationGroup(@RequestBody @Valid CreateTranslationGroupRequest request) {
        translationGroupService.createTranslationGroup(request);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                messageSource.getMessage(
                        "translation.group.created.success",
                        null,
                        LocaleContextHolder.getLocale()
                ),
                null
            )
        );
    }

    @PostMapping("/add-member")
    @PreAuthorize("hasAnyAuthority('TG_ADMIN', 'TG_MOD') or hasRole('ADMIN')")
    @Operation(summary = "Add a member to a translation group")
    public ResponseEntity<ApiResponse<?>> addMemberToTranslationGroup(@RequestParam UUID groupId, @RequestParam UUID userId) {
        translationGroupService.addTranslationGroupMember(groupId, userId);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                messageSource.getMessage(
                        "member.added.success",
                        null,
                        LocaleContextHolder.getLocale()
                ),
                null
            )
        );
    }

}
