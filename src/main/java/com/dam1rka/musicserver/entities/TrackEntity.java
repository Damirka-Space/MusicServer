package com.dam1rka.musicserver.entities;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @NonNull
    @ManyToMany(fetch = FetchType.EAGER)
    private List<AuthorEntity> author;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<AlbumEntity> album;

    public TrackEntity() {

    }
}
