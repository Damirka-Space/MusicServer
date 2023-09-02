package com.dam1rka.musicserver.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;

import jakarta.persistence.*;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    private Integer duration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private AlbumEntity album;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<AuthorEntity> authors;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<GenreEntity> genres;

    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date updated;

    @Nullable
    private Long fileId;

    public TrackEntity() {

    }
}
