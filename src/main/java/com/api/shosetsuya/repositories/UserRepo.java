package com.api.shosetsuya.repositories;

import com.api.shosetsuya.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<Users, UUID> {
    Optional<Users> findByUsername(String username);
}
