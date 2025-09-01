package com.api.shosetsuya.repositories;

import com.api.shosetsuya.models.entities.TranslationGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TranslationGroupRepo extends JpaRepository<TranslationGroup, UUID> {
    boolean existsByName(String name);
}
