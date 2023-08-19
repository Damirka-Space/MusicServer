package com.dam1rka.musicserver.entities.channel;

import com.dam1rka.musicserver.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class ChannelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Date created;

    @OneToOne
    private ChannelChat chat;

    @OneToOne
    private ChannelQueue queue;

    @OneToOne
    private UserEntity owner;

    @OneToMany
    private List<ChannelUserEntity> users;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}
