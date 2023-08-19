package com.dam1rka.musicserver.repositories.channel;

import com.dam1rka.musicserver.entities.channel.ChannelMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMessageRepository extends JpaRepository<ChannelMessage, Long> {
}
