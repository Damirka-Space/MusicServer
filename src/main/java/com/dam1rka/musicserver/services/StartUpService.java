package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.AlbumTypeEntity;
import com.dam1rka.musicserver.entities.AlbumTypeEnum;
import com.dam1rka.musicserver.repositories.AlbumTypeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StartUpService {

    private final AlbumTypeRepository albumTypeRepository;
    private final PlaylistService playlistService;

    @Autowired
    public StartUpService(PlaylistService playlistService, AlbumTypeRepository albumTypeRepository) {
        this.playlistService = playlistService;
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

        playlistService.updateAll();


    }
}
