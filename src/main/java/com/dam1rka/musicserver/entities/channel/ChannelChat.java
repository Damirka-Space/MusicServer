package com.dam1rka.musicserver.entities.channel;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ChannelChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    List<ChannelMessage> messages;
}
