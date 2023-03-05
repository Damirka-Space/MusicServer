package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.AlbumEntity;
import com.dam1rka.musicserver.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

//    AlbumEntity findByTitle(String title);
    AlbumEntity findByTitleAndAuthorsIn(String title, List<AuthorEntity> authors);

    @Query(value = "SELECT ae.id, ae.title, ae.description, ae.album_type_entity_id, ae.created, ae.updated, ae.image_id FROM liked_albums AS ls " +
            "JOIN liked_albums_albums laa on ls.id = laa.liked_albums_id " +
            "JOIN album_entity ae on laa.albums_id = ae.id " +
            "WHERE ls.user_id = ?1 AND ae.album_type_entity_id = ?2 ORDER BY ae.created", nativeQuery = true)
    List<AlbumEntity> findLikedAlbumsByUserAndAlbumType(long userId, long albumType);
}
