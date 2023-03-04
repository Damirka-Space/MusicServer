package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LikeService {

    private final LikedAlbumsRepository likedAlbumsRepository;
    private final AlbumTypeRepository albumTypeRepository;
    private final AlbumRepository albumRepository;
    private final ImageRepository imageRepository;
    private final TrackRepository trackRepository;

    @Autowired
    public LikeService(LikedAlbumsRepository likedAlbumsRepository, AlbumTypeRepository albumTypeRepository,
                       AlbumRepository albumRepository, ImageRepository imageRepository, TrackRepository trackRepository) {
        this.likedAlbumsRepository = likedAlbumsRepository;
        this.albumTypeRepository = albumTypeRepository;
        this.albumRepository = albumRepository;
        this.imageRepository = imageRepository;
        this.trackRepository = trackRepository;
    }

    private AlbumEntity createLikedAlbum(UserEntity user) {
        AlbumEntity album = new AlbumEntity();
        album.setAlbumTypeEntity(albumTypeRepository.findById((long) (AlbumTypeEnum.PLAYLIST.ordinal() + 1)).orElse(null));

        album.setTitle("Любимые треки");
        album.setDescription("Всего треков - 0");

        Date now = new Date();

        album.setCreated(now);
        album.setUpdated(now);

        album.setImage(imageRepository.findById(1L).orElse(null));

        return albumRepository.save(album);
    }

    public void initializeUser(UserEntity user) {
        if(Objects.isNull(likedAlbumsRepository.findByUser(user))) {
            LikedAlbums likedAlbums = new LikedAlbums();

            likedAlbums.setUser(user);

            List<AlbumEntity> albums = new LinkedList<>();

            albums.add(createLikedAlbum(user));

            likedAlbums.setAlbums(albums);

            likedAlbumsRepository.save(likedAlbums);
        }
    }

    public boolean containAlbum(UserEntity user, AlbumEntity album) {
        LikedAlbums l = likedAlbumsRepository.findByUser(user);

        return l.getAlbums().contains(album);
    }

    public List<AlbumEntity> getLikedByType(UserEntity user, AlbumTypeEnum albumType) {
        LikedAlbums l = likedAlbumsRepository.findByUserAndAlbumType(user.getId(),
                (long) albumType.ordinal() + 1);

        if(Objects.nonNull(l))
            return l.getAlbums();

        return new LinkedList<>();
    }

    public void likeTrack(UserEntity user, long trackId) {
        LikedAlbums l = likedAlbumsRepository.findByUserAndAlbumType(user.getId(), AlbumTypeEnum.PLAYLIST.ordinal() + 1);
        AlbumEntity likedAlbums = l.getAlbums().get(0);

        Optional<TrackEntity> track =  trackRepository.findById(trackId);

        track.ifPresent(trackEntity -> {
            List<TrackEntity> tracks = likedAlbums.getTracks();

            if(!tracks.contains(track.get()))
                tracks.add(trackEntity);
            else
                tracks.remove(trackEntity);

            likedAlbums.setDescription("Всего треков - " + likedAlbums.getTracks().size());
            albumRepository.save(likedAlbums);
        });
    }

    public void likeAlbum(UserEntity user, long id) {
        LikedAlbums l = likedAlbumsRepository.findByUser(user);

        Optional<AlbumEntity> album = albumRepository.findById(id);

        album.ifPresent(albumEntity -> {
            if(l.getAlbums().contains(album.get()))
                l.getAlbums().remove(album.get());
            else
                l.getAlbums().add(album.get());

            likedAlbumsRepository.save(l);
        });
    }
}
