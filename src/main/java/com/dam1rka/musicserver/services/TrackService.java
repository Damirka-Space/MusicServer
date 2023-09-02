package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.TrackUploadDto;
import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class TrackService {

    @Value("${music.format}")
    private String format;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AlbumTypeRepository albumTypeRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    private TrackRepository trackRepository;

    private FileService fileService;

    @Autowired
    TrackService(TrackRepository trackRepository, FileService fileService) {
        this.trackRepository = trackRepository;
        this.fileService = fileService;
    }

    public void saveTrack(TrackUploadDto trackUploadDto) {
//        if(!Objects.requireNonNull(trackUploadDto.getTrack().getOriginalFilename()).contains(format))
//            throw new RuntimeException("Invalid file");
//
//        String title = trackUploadDto.getTitle();
//
//        TrackEntity trackEntity = trackRepository.findByTitle(title);
//
//        if(Objects.nonNull(trackEntity))
//            throw new RuntimeException("Track already exist!");
//
//        trackEntity = new TrackEntity();
//        trackEntity.setTitle(title);
//
//        Date now = new Date();
//        trackEntity.setCreated(now);
//        trackEntity.setUpdated(now);
//
//        // create author for track
//        {
//            List<AuthorEntity> authors = new LinkedList<>();
//
//            String[] a_str = trackUploadDto.getAuthor().split(", ");
//
//            for(String a : a_str) {
//                AuthorEntity author = authorRepository.findByName(a);
//                if(Objects.isNull(author)){
//                    author = new AuthorEntity();
//                    author.setName(a);
//                    author.setCreated(now);
//                    author.setUpdated(now);
//                    author = authorRepository.save(author);
//                }
//                authors.add(author);
//            }
//
//            trackEntity.setAuthors(authors);
//        }
//
//        // load genres for track
//        {
//            List<GenreEntity> genres = new LinkedList<>();
//
//            String[] g_str = trackUploadDto.getGenre().split(", ");
//
//            for(String g : g_str) {
//                GenreEntity genre = genreRepository.findByName(g);
//                if(Objects.isNull(genre)){
//                    genre = new GenreEntity();
//                    genre.setName(g);
//                    genre.setCreated(now);
//                    genre.setUpdated(now);
//                    genre = genreRepository.save(genre);
//                }
//                genres.add(genre);
//            }
//
//            trackEntity.setGenres(genres);
//        }
//
//        // create album
//
//        PrimaryAlbumEntity album = primaryAlbumRepository.findByTitle(trackUploadDto.getAlbum());
//
//        if(Objects.isNull(album)) {
//            album = new PrimaryAlbumEntity();
//            album.setTitle(trackUploadDto.getAlbum());
//            album.setCreated(now);
//
//            // create image
//            {
//                album.setImage(imageService.saveImage(trackUploadDto.getImage(), album.getTitle()));
//            }
//
//            // create author for album
//            {
//                List<AuthorEntity> authors = album.getAuthors();
//
//                if(Objects.isNull(authors)) {
//                    authors = new LinkedList<>();
//                    String[] a_str = trackUploadDto.getAlbums_author().split(", ");
//
//                    for(String a : a_str) {
//                        AuthorEntity author = authorRepository.findByName(a);
//                        if(Objects.isNull(author)){
//                            author = new AuthorEntity();
//                            author.setName(a);
//                            author.setCreated(now);
//                            author.setUpdated(now);
//                            author = authorRepository.save(author);
//                        }
//                        authors.add(author);
//                    }
//
//                    album.setAuthors(authors);
//                }
//            }
//        }
//
//        album.setUpdated(now);
//
//        album = primaryAlbumRepository.save(album);
//
//        trackEntity.setAlbum(album);
//        trackEntity = trackRepository.save(trackEntity);
//
//        // Add track to album
//        {
//            List<TrackEntity> tracks = album.getTracks();
//            if(Objects.isNull(tracks)) {
//                tracks = new LinkedList<>();
//                album.setAlbumTypeEntity(albumTypeRepository.findById((long) (AlbumTypeEnum.SINGLE.ordinal() + 1)).orElse(null));
//            }
//            else
//                album.setAlbumTypeEntity(albumTypeRepository.findById((long) (AlbumTypeEnum.ALBUM.ordinal() + 1)).orElse(null));
//
//            tracks.add(trackEntity);
//            album.setTracks(tracks);
//
//            primaryAlbumRepository.save(album);
//        }
//
//        fileService.saveTrack(trackEntity.getId(), trackUploadDto.getTrack());
    }

    public TrackEntity getTrackById(Long id) {
        return trackRepository.findById(id).orElse(null);
    }

    public byte[] loadTrackById(Long id) throws RuntimeException {
        TrackEntity track = getTrackById(id);

        if(Objects.isNull(track)) {
            throw new RuntimeException("Track not found");
        }
        return fileService.loadTrack(id);
    }

}
