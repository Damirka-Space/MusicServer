package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final BlockRepository blockRepository;
    private final AlbumRepository albumRepository;
    private final AlbumTypeRepository albumTypeRepository;
    private final AuthorRepository authorRepository;
    private final ImageRepository imageRepository;
    private final TrackRepository trackRepository;
    private final GenreRepository genreRepository;

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

            ImageEntity image = imageRepository.findByUrl(imageUrl);

            if(Objects.isNull(image)) {
                image = new ImageEntity();
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
            albums = getAlbumEntities(album, block);
        }

        block.setAlbums(albums);
        blockRepository.save(block);
    }

    @NonNull
    private List<AlbumEntity> getAlbumEntities(AlbumEntity album, BlockEntity block) {
        List<AlbumEntity> albums = block.getAlbums();

        boolean exist = false;

        for(AlbumEntity al : albums) {
            if(Objects.equals(al.getId(), album.getId())) {
                exist = true;
                break;
            }
        }
        if(!exist)
            albums.add(album);
        return albums;
    }

    private List<TrackEntity> randomTracks(int count) {
        long tracksCount = trackRepository.count();

        if(tracksCount < count)
            count = (int) tracksCount;

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
        updateAllInOnePlaylist();
        updatePlaylistOfWeek();
        updatePlaylistOfDay();
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

    private void updateBlock(AlbumEntity album, String title, long id) {
        BlockEntity block = blockRepository.findById(id).orElse(null);
        List<AlbumEntity> albums;

        if(Objects.isNull(block)) {
            block = new BlockEntity();
            block.setId(id);
            block.setTitle(title);
            albums = Collections.singletonList(album);
        } else {
            albums = getAlbumEntities(album, block);
        }

        block.setAlbums(albums);
        blockRepository.save(block);
    }

}
