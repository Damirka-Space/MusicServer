package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private BlockRepository blockRepository;
    private AlbumRepository albumRepository;
    private PrimaryAlbumRepository primaryAlbumRepository;
    private AlbumTypeRepository albumTypeRepository;
    private AuthorRepository authorRepository;
    private ImageRepository imageRepository;
    private TrackRepository trackRepository;
    private GenreRepository genreRepository;

    @Autowired
    public PlaylistService(BlockRepository blockRepository, AlbumRepository albumRepository, AuthorRepository authorRepository,
                            AlbumTypeRepository albumTypeRepository, ImageRepository imageRepository, TrackRepository trackRepository,
                           GenreRepository genreRepository, PrimaryAlbumRepository primaryAlbumRepository) {
        this.blockRepository = blockRepository;
        this.albumRepository = albumRepository;
        this.authorRepository = authorRepository;
        this.albumTypeRepository = albumTypeRepository;
        this.imageRepository = imageRepository;
        this.trackRepository = trackRepository;
        this.genreRepository = genreRepository;
        this.primaryAlbumRepository = primaryAlbumRepository;
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
        updatePlaylistOfDay();
        updatePlaylistOfWeek();
        updateAllInOnePlaylist();

        updateMetalPlaylist();

        try {
            updateElectronicPlaylist();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        List<String> genres = new LinkedList<>() {{
            add("Альтернатива");
            add("Альтернативный метал");
        } };
        List<TrackEntity> metalTracks = trackRepository.findAllByGenres(genres);

        AlbumEntity album = updateAlbum(4L, "Метал", "альтернатива", "metall_test", metalTracks);

        updateBlock(albumRepository.save(album), "Любителям металла посвящается!", 2L);
    }

    public void updateElectronicPlaylist() {
        PrimaryAlbumEntity solai = primaryAlbumRepository.findByTitle("Solai");
        PrimaryAlbumEntity afterDark = primaryAlbumRepository.findByTitle("After Dark");
        if(Objects.isNull(solai) || Objects.isNull(afterDark))
            return;

        updateBlock(fromPrimary(solai), "Вперёд в будущее!", 3L);
        updateBlock(fromPrimary(afterDark), "Вперёд в будущее!", 3L);
    }

    private AlbumEntity fromPrimary(PrimaryAlbumEntity primaryAlbum) {
        AlbumEntity album = albumRepository.findByTitle(primaryAlbum.getTitle());

        if(Objects.isNull(album)) {
            album = new AlbumEntity();
            album.setTitle(primaryAlbum.getTitle());
            album.setAuthors(primaryAlbum.getAuthors());
            album.setDescription(primaryAlbum.getAuthors().stream().map(AuthorEntity::getName).collect(Collectors.joining(", ")));
            album.setImage(primaryAlbum.getImage());

            Date now = new Date();

            album.setCreated(now);
            album.setUpdated(now);

            album.setAlbumTypeEntity(primaryAlbum.getAlbumTypeEntity());

            album.setTracks(primaryAlbum.getTracks());
        }
        return album;
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
