package com.dam1rka.musicserver.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private PrimaryAlbumEntity album;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<AuthorEntity> authors;

    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date updated;

    public TrackEntity() {

    }
}
