package com.dam1rka.musicserver.repositories;

import com.dam1rka.musicserver.entities.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<BlockEntity, Long> {
}
