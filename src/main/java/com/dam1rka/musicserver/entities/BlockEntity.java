package com.dam1rka.musicserver.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class BlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    List<AlbumEntity> albums;

    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date updated;

    public BlockEntity() {

    }
}
