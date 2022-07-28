package com.dam1rka.musicserver.entities;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.persistence.*;
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


    public BlockEntity() {

    }
}
