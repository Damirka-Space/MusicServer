package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.LikedAlbums;
import com.dam1rka.musicserver.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface LikedAlbumsRepository extends JpaRepository<LikedAlbums, Long> {
    LikedAlbums findByUser(UserEntity user);

    @Query(value = "SELECT ls.id, ls.user_id, laa.albums_id FROM liked_albums AS ls " +
            "JOIN liked_albums_albums laa on ls.id = laa.liked_albums_id " +
            "JOIN album_entity ae on laa.albums_id = ae.id " +
            "WHERE ls.user_id = ?1 AND ae.album_type_entity_id = ?2 ORDER BY ae.created", nativeQuery = true)
    LikedAlbums findByUserAndAlbumType(long userId, long albumType);
}
