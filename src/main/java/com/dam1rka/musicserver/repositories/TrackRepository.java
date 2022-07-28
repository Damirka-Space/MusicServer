package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<TrackEntity, Long> {

}
