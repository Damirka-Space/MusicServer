package com.dam1rka.musicserver.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class PrimaryAlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;
    @NonNull
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    private AlbumTypeEntity albumTypeEntity;

    @OneToOne(fetch = FetchType.EAGER)
    private ImageEnitiy image;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<AuthorEntity> authors;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<TrackEntity> tracks;

    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date updated;

    public PrimaryAlbumEntity() {

    }
}
