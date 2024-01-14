package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.AlbumTypeEntity;
import com.dam1rka.musicserver.entities.AlbumTypeEnum;
import com.dam1rka.musicserver.entities.UserEntity;
import com.dam1rka.musicserver.repositories.AlbumTypeRepository;
import com.dam1rka.musicserver.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StartUpService {

    private final AlbumTypeRepository albumTypeRepository;
    private final PlaylistService playlistService;
    private final UserRepository userRepository;
    private final LikeService likeService;

    @Autowired
    public StartUpService(PlaylistService playlistService, AlbumTypeRepository albumTypeRepository, UserRepository userRepository, LikeService likeService) {
        this.playlistService = playlistService;
        this.albumTypeRepository = albumTypeRepository;
        this.userRepository = userRepository;
        this.likeService = likeService;
    }

    private void initializeAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        for(UserEntity user : users) {
            likeService.initializeUser(user);
        }
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

//        initializeAllUsers();
    }
}
