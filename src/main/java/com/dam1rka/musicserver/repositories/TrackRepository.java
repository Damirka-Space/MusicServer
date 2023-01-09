package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.AlbumEntity;
import com.dam1rka.musicserver.entities.AuthorEntity;
import com.dam1rka.musicserver.entities.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
    TrackEntity findByTitle(String title);
    TrackEntity findByTitleAndAlbumAndAuthorsIn(String title, AlbumEntity album, List<AuthorEntity> authors);

    @Query(value = "SELECT * FROM track_entity LEFT JOIN track_entity_genres ON track_entity_id = id LEFT JOIN genre_entity ge ON genres_id = ge.id WHERE ge.name IN :genres", nativeQuery = true)
    List<TrackEntity> findAllByGenres(List<String> genres);

    List<TrackEntity> findAllByAlbum(AlbumEntity album);
}
