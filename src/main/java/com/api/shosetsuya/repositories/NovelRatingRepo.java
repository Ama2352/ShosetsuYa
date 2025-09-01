package com.api.shosetsuya.repositories;

import com.api.shosetsuya.models.entities.NovelRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NovelRatingRepo extends JpaRepository<NovelRating, UUID> {
}
