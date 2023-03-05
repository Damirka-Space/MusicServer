package com.dam1rka.musicserver.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Data
public class ListenHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreatedDate
    private Date listened;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrackEntity track;
}
