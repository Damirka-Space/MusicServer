package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
    TrackEntity findByTitle(String title);
}
