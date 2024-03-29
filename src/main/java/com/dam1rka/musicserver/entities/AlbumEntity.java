package com.dam1rka.musicserver.entities;


import com.fasterxml.jackson.annotation.JsonInclude;

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
public class AlbumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;
    @NonNull
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private AlbumTypeEntity albumTypeEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    private ImageEntity image;

    @JsonInclude
    @Transient
    private String imageUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<AuthorEntity> authors;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<TrackEntity> tracks;

    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date updated;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = false;

    public AlbumEntity() {
    }
}
