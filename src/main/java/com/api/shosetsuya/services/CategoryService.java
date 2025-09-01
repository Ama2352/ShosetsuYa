package com.api.shosetsuya.services;

import com.api.shosetsuya.helpers.exceptions.ResourceNotFoundException;
import com.api.shosetsuya.models.dtos.catogry.CategoryDTO;
import com.api.shosetsuya.models.entities.Category;
import com.api.shosetsuya.models.entities.NovelTranslation;
import com.api.shosetsuya.repositories.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(category -> new CategoryDTO(
                        category.getCategoryId().toString(),
                        category.getName())
                )
                .collect(Collectors.toList());
    }

    public String getCategoryById(UUID categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return category.getName();
    }

    public CategoryDTO addCategory(String name) {
        Category newCategory = categoryRepo.save(new Category(name));
        return new CategoryDTO(newCategory.getCategoryId().toString(), newCategory.getName());
    }

    public CategoryDTO updateCategory(UUID categoryId, String name) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        category.setName(name);
        Category updatedCategory = categoryRepo.save(category);
        return new CategoryDTO(updatedCategory.getCategoryId().toString(), updatedCategory.getName());
    }

    public void deleteCategory(UUID categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        for (NovelTranslation novel : category.getNovels()) {
            novel.getCategories().remove(category);
        }
        categoryRepo.delete(category);
    }
}
