package com.dam1rka.musicserver.repositories.channel;

import com.dam1rka.musicserver.entities.channel.ChannelQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelQueueRepository extends JpaRepository<ChannelQueue, Long> {
}
