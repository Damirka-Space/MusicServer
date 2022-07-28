package com.dam1rka.musicserver.entities;


import lombok.Data;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class AlbumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;
    @NonNull
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    private ImageEnitiy image;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<AuthorEntity> authors;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<TrackEntity> tracks;

    public AlbumEntity() {

    }
}
