package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.AlbumTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumTypeRepository extends JpaRepository<AlbumTypeEntity, Long> {
}
