package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.PrimaryAlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrimaryAlbumRepository extends JpaRepository<PrimaryAlbumEntity, Long> {

    PrimaryAlbumEntity findByTitle(String album);
}
