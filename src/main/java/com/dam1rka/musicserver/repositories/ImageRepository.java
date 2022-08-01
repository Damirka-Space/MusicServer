package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.ImageEnitiy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEnitiy, Long> {

    ImageEnitiy findByUrl(String url);
}
