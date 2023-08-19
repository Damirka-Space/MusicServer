package com.dam1rka.musicserver.entities.channel;

import com.dam1rka.musicserver.entities.TrackEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ChannelQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<ChannelQueueAction> actions;

    @OneToMany
    private List<TrackEntity> tracks;
}
