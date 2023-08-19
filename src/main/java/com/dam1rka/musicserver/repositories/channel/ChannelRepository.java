package com.dam1rka.musicserver.repositories.channel;

import com.dam1rka.musicserver.entities.channel.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<ChannelEntity, Long> {
}
