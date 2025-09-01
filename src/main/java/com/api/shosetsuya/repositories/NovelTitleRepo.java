package com.api.shosetsuya.repositories;

import com.api.shosetsuya.models.entities.NovelTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NovelTitleRepo extends JpaRepository<NovelTitle, UUID> {
}
