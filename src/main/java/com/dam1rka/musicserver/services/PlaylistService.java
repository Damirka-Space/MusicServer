package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlaylistService {

    private BlockRepository blockRepository;
    private AlbumRepository albumRepository;
    private AlbumTypeRepository albumTypeRepository;
    private AuthorRepository authorRepository;
    private ImageRepository imageRepository;
    private TrackRepository trackRepository;
    private GenreRepository genreRepository;

    @Autowired
    public PlaylistService(BlockRepository blockRepository, AlbumRepository albumRepository, AuthorRepository authorRepository,
                            AlbumTypeRepository albumTypeRepository, ImageRepository imageRepository, TrackRepository trackRepository,
                           GenreRepository genreRepository) {
        this.blockRepository = blockRepository;
        this.albumRepository = albumRepository;
        this.authorRepository = authorRepository;
        this.albumTypeRepository = albumTypeRepository;
        this.imageRepository = imageRepository;
        this.trackRepository = trackRepository;
        this.genreRepository = genreRepository;
    }

    private AlbumEntity updateAlbum(Long id, String title, String description, String imageUrl, List<TrackEntity> tracks) {
        AlbumEntity album = albumRepository.findById(id).orElse(null);

        Date now = new Date();

        if(Objects.isNull(album)) {
            album = new AlbumEntity();
            album.setId(id);
            album.setTitle(title);
            album.setDescription(description);
            album.setCreated(now);
            album.setUpdated(now);

            ImageEnitiy image = imageRepository.findByUrl(imageUrl);

            if(Objects.isNull(image)) {
                image = new ImageEnitiy();
                image.setUrl(imageUrl);

                imageRepository.save(image);
            }

            album.setImage(image);

            AlbumTypeEntity type = albumTypeRepository.findById((long) (AlbumTypeEnum.PLAYLIST.ordinal() + 1)).orElseThrow();

            album.setAlbumTypeEntity(type);
        }
        album.setUpdated(now);
        album.setTracks(tracks);
        return albumRepository.save(album);
    }

    private void updateWelcomeBlock(AlbumEntity album) {
        BlockEntity block = blockRepository.findById(1L).orElse(null);

        List<AlbumEntity> albums;

        if(Objects.isNull(block)) {
            block = new BlockEntity();
            block.setId(1L);
            block.setTitle("Добро пожаловать!");
            albums = Collections.singletonList(album);
        } else {
            albums = block.getAlbums();

            boolean exist = false;

            for(AlbumEntity al : albums) {
                if(Objects.equals(al.getId(), album.getId())) {
                    exist = true;
                    break;
                }
            }
            if(!exist)
                albums.add(album);
        }

        block.setAlbums(albums);
        blockRepository.save(block);
    }

    private List<TrackEntity> randomTracks(int count) {
        long tracksCount = trackRepository.count();

        List<Long> ids = new LinkedList<>();

        for(int i = 0; i < count; i++) {
            long id = (long) (Math.random() * tracksCount);

            while(ids.contains(id))
                id = (long) (Math.random() * tracksCount);

            ids.add(id);
        }

        return trackRepository.findAllById(ids);
    }

    public void updateAll() {
        updatePlaylistOfDay();
        updatePlaylistOfWeek();
        updateAllInOnePlaylist();

        updateMetalPlaylist();
    }

    public void updatePlaylistOfDay() {
        List<TrackEntity> dayTracks = randomTracks(10);

        AlbumEntity album = updateAlbum(3L, "Плейлист дня", "Что послушать сегодня", "PlaylistOfDay", dayTracks);

        updateWelcomeBlock(albumRepository.save(album));
    }

    public void updatePlaylistOfWeek() {
        List<TrackEntity> weekTracks = randomTracks(20);

        AlbumEntity album = updateAlbum(2L, "Открытие недели", "Треки этой недели", "PlaylistOfWeek", weekTracks);

        updateWelcomeBlock(albumRepository.save(album));
    }

    public void updateAllInOnePlaylist() {
        // Load all tracks in one playlist.
        List<TrackEntity> allTracks = trackRepository.findAll();

        AlbumEntity album = updateAlbum(1L, "Всё в одном", "Первый и не повторимый", "InOne", allTracks);

        updateWelcomeBlock(albumRepository.save(album));
    }

    public void updateMetalPlaylist() {
        List<TrackEntity> metalTracks = trackRepository.findAllByGenre(Collections.singletonList(genreRepository.findByName("Альтернативный метал")));

        AlbumEntity album = updateAlbum(4L, "Метал", "альтернатива", "", metalTracks);

        updateMetalBlock(albumRepository.save(album));
    }

    private void updateMetalBlock(AlbumEntity album) {
        BlockEntity block = blockRepository.findById(2L).orElse(null);

        List<AlbumEntity> albums;

        if(Objects.isNull(block)) {
            block = new BlockEntity();
            block.setId(2L);
            block.setTitle("Любителям металла посвящается!");
            albums = Collections.singletonList(album);
        } else {
            albums = block.getAlbums();

            boolean exist = false;

            for(AlbumEntity al : albums) {
                if(Objects.equals(al.getId(), album.getId())) {
                    exist = true;
                    break;
                }
            }
            if(!exist)
                albums.add(album);
        }

        block.setAlbums(albums);
        blockRepository.save(block);
    }

}
