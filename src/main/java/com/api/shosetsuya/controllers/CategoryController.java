package com.api.shosetsuya.controllers;

import com.api.shosetsuya.helpers.ApiResponse;
import com.api.shosetsuya.models.dtos.catogry.CategoryDTO;
import com.api.shosetsuya.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerToken")
@RequiredArgsConstructor
@RequestMapping("/category")
@Tag(name = "Category")
@PreAuthorize("hasRole('USER')")
public class CategoryController {

    private final CategoryService categoryService;
    private final MessageSource messageSource;

    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        messageSource.getMessage(
                                "data.fetch.success",
                                null,
                                LocaleContextHolder.getLocale()
                        ),
                        categoryService.getAllCategories()
                )
        );
    }

    @PostMapping
    @Operation(summary = "Add a new category")
    @PreAuthorize("hasAuthority('TG_TRANSLATOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDTO>> addCategory(String name) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        messageSource.getMessage(
                                "data.add.success",
                                null,
                                LocaleContextHolder.getLocale()
                        ),
                        categoryService.addCategory(name)
                )
        );
    }

    @PutMapping
    @Operation(summary = "Update an existing category by ID")
    @PreAuthorize("hasAuthority('TG_TRANSLATOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@RequestParam UUID categoryId, @RequestParam String categoryName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        messageSource.getMessage(
                                "data.update.success",
                                null,
                                LocaleContextHolder.getLocale()
                        ),
                        categoryService.updateCategory(categoryId, categoryName)
                )
        );
    }

    @DeleteMapping
    @Operation(summary = "Delete a category by ID")
    @PreAuthorize("hasAuthority('TG_TRANSLATOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@RequestParam String categoryId) {
        categoryService.deleteCategory(java.util.UUID.fromString(categoryId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        true,
                        messageSource.getMessage(
                                "data.delete.success",
                                null,
                                LocaleContextHolder.getLocale()
                        ),
                        null
                )
        );
    }
}
