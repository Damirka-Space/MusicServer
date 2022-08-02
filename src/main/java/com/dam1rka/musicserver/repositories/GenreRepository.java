package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    GenreEntity findByName(String name);

}
