package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.AlbumEntity;
import com.dam1rka.musicserver.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

//    AlbumEntity findByTitle(String title);
    AlbumEntity findByTitleAndAuthorsIn(String title, List<AuthorEntity> authors);
}
