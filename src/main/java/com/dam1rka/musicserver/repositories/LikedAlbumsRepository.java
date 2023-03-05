package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.LikedAlbums;
import com.dam1rka.musicserver.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikedAlbumsRepository extends JpaRepository<LikedAlbums, Long> {
    LikedAlbums findByUser(UserEntity user);
}
