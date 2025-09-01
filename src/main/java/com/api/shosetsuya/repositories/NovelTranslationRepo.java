package com.api.shosetsuya.repositories;

import com.api.shosetsuya.models.entities.NovelTranslation;
import com.api.shosetsuya.models.entities.TranslationGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NovelTranslationRepo extends JpaRepository<NovelTranslation, UUID> {
    List<NovelTranslation> findAllByTranslationGroup(TranslationGroup group);
}
