package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

    AlbumEntity findByTitle(String title);
}
