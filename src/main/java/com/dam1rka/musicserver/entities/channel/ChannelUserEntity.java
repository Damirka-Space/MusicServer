package com.dam1rka.musicserver.entities.channel;

import com.dam1rka.musicserver.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class ChannelUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date created;

    private ChannelActionEnum action;

    @ManyToOne
    private UserEntity user;
}
