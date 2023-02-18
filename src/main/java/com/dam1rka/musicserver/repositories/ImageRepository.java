package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    ImageEntity findByUrl(String url);
}
