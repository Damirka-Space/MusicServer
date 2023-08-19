package com.dam1rka.musicserver.repositories.channel;

import com.dam1rka.musicserver.entities.channel.ChannelUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelUserRepository extends JpaRepository<ChannelUserEntity, Long> {
}
