package com.api.shosetsuya.repositories;

import com.api.shosetsuya.models.entities.TranslationGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TranslationGroupMemberRepo extends JpaRepository<TranslationGroupMember, UUID> {
}
