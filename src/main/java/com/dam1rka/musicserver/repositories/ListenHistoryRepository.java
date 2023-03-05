package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.ListenHistoryEntity;
import com.dam1rka.musicserver.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListenHistoryRepository extends JpaRepository<ListenHistoryEntity, Long> {

    List<ListenHistoryEntity> findAllByUserOrderByListenedDesc(UserEntity user);

}
