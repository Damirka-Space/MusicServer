package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class StartUpService {

    private BlockRepository blockRepository;
    private AlbumRepository albumRepository;
    private AuthorRepository authorRepository;
    private ImageRepository imageRepository;
    private TrackRepository trackRepository;
    private AlbumTypeRepository albumTypeRepository;

    @Autowired
    public StartUpService(BlockRepository blockRepository, AlbumRepository albumRepository, AuthorRepository authorRepository,
                          ImageRepository imageRepository, TrackRepository trackRepository, AlbumTypeRepository albumTypeRepository) {
        this.blockRepository = blockRepository;
        this.albumRepository = albumRepository;
        this.authorRepository = authorRepository;
        this.imageRepository = imageRepository;
        this.trackRepository = trackRepository;
        this.albumTypeRepository = albumTypeRepository;
    }

    @PostConstruct
    public void startUp() {

        // Check album types.
        for (AlbumTypeEnum v : AlbumTypeEnum.values()) {
            AlbumTypeEntity type = albumTypeRepository.findById((long) (v.ordinal() + 1)).orElse(null);
            if(Objects.isNull(type)) {
                type = new AlbumTypeEntity();
                type.setId((long) (v.ordinal() + 1));
                type.setTitle(v.name());
                albumTypeRepository.save(type);
            }
        }

        // Load all tracks in one playlist.
        List<TrackEntity> allTracks = trackRepository.findAll();

        AlbumEntity album = albumRepository.findById(1L).orElse(null);
        if(Objects.isNull(album)) {
            album = new AlbumEntity();
            album.setId(1L);
            album.setTitle("Всё в одном");
            album.setDescription("Первый и не повторимый");

            ImageEnitiy image = imageRepository.findById(1L).orElse(null);

            if(Objects.isNull(image)) {
                image = new ImageEnitiy();
                image.setId(1L);
                image.setUrl("InOne.png");

                imageRepository.save(image);
            }

            album.setImage(image);
        }

        album.setTracks(allTracks);

        albumRepository.save(album);

        BlockEntity block = blockRepository.findById(1L).orElse(null);

        if(Objects.isNull(block)) {
            block = new BlockEntity();
            block.setId(1L);
            block.setTitle("Добро пожаловать!");
            block.setAlbums(Collections.singletonList(album));

            blockRepository.save(block);
        }


        ///

    }
}
