package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.GenreEntity;
import com.dam1rka.musicserver.entities.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
    TrackEntity findByTitle(String title);

    @Query("SELECT te FROM TrackEntity te WHERE te.genres = :genre")
    List<TrackEntity> findAllByGenre(GenreEntity genre);
}
