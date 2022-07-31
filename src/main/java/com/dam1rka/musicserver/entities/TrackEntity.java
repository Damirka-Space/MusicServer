package com.dam1rka.musicserver.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @OneToOne
    private PrimaryAlbumEntity album;

    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date updated;

    public TrackEntity() {

    }
}
