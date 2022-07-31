package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    AuthorEntity findByName(String name);
}
