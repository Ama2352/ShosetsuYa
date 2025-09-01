package com.api.shosetsuya.repositories;

import com.api.shosetsuya.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepo extends JpaRepository<Category, UUID> {
}
