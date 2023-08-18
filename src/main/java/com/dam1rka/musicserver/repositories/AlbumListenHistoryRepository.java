package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.AlbumListenHistoryEntity;
import com.dam1rka.musicserver.entities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumListenHistoryRepository extends JpaRepository<AlbumListenHistoryEntity, Long> {

    List<AlbumListenHistoryEntity> findAllByUserOrderByListenedDesc(UserEntity user, Pageable pageable);
}
