package com.dam1rka.musicserver.entities.channel;

import com.dam1rka.musicserver.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class ChannelMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String message;

    private Date created;

    @ManyToOne
    private UserEntity sender;
}
