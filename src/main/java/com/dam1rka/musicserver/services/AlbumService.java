package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.AlbumUploadDto;
import com.dam1rka.musicserver.dtos.TrackDto;
import com.dam1rka.musicserver.dtos.TrackUploadNewDto;
import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class AlbumService {

    private PrimaryAlbumRepository primaryAlbumRepository;
    private AlbumRepository albumRepository;
    private TrackRepository trackRepository;
    private AuthorRepository authorRepository;
    private GenreRepository genreRepository;
    private AlbumTypeRepository albumTypeRepository;
    private ImageService imageService;
    private FileService fileService;

    @Autowired
    public AlbumService(PrimaryAlbumRepository primaryAlbumRepository, AlbumRepository albumRepository, TrackRepository trackRepository,
                        AuthorRepository authorRepository, GenreRepository genreRepository, ImageService imageService, FileService fileService,
                        AlbumTypeRepository albumTypeRepository) {
        this.primaryAlbumRepository = primaryAlbumRepository;
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.albumTypeRepository = albumTypeRepository;
        this.imageService = imageService;
        this.fileService = fileService;
    }

    public AlbumEntity getAlbum(Long id) throws RuntimeException {
        AlbumEntity album = albumRepository.findById(id).orElse(null);
        if(Objects.isNull(album))
            throw new RuntimeException("Album not found");

        return album;
    }

    public PrimaryAlbumEntity getPrimaryAlbum(Long id) throws RuntimeException {
        PrimaryAlbumEntity album = primaryAlbumRepository.findById(id).orElse(null);
        if(Objects.isNull(album))
            throw new RuntimeException("Album not found");

        return album;
    }

    public List<TrackDto> getTracks(Long id) throws RuntimeException {
        AlbumEntity album = getAlbum(id);

        List<TrackDto> tracks = new LinkedList<>();

        for(TrackEntity track : album.getTracks()) {
            TrackDto t = new TrackDto();

            t.setId(track.getId());
            t.setTitle(track.getTitle());

            List<AuthorEntity> authors = track.getAuthors();

            if(Objects.nonNull(authors)) {
                List<Long> ids = new LinkedList<>();
                List<String> ath = new LinkedList<>();
                for(AuthorEntity author : authors) {
                    ids.add(author.getId());
                    ath.add(author.getName());
                }

                t.setAuthorId(ids);
                t.setAuthor(ath);
            }

            PrimaryAlbumEntity primaryAlbum = track.getAlbum();
            if(Objects.nonNull(primaryAlbum)) {
                t.setAlbumId(primaryAlbum.getId());
                t.setAlbum(primaryAlbum.getTitle());
            }
            tracks.add(t);
        }
        return tracks;
    }

    private PrimaryAlbumEntity loadPrimaryAlbum(Long id) {
        PrimaryAlbumEntity album = getPrimaryAlbum(id);
        if(Objects.isNull(album))
            throw new RuntimeException("Album not found");

        if(Objects.isNull(album.getImage()))
            throw new RuntimeException("No image in album");

        return  album;
    }

    public byte[] loadImage(Long id) {
        return imageService.getImage(loadPrimaryAlbum(id).getImage().getId());
    }

    public byte[] loadSmallImage(Long id) {
        return imageService.getSmailImage(loadPrimaryAlbum(id).getImage().getId());
    }

    public byte[] loadMediumImage(Long id) {
        return imageService.getMediumImage(loadPrimaryAlbum(id).getImage().getId());
    }

    public void uploadAlbum(AlbumUploadDto albumUploadDto) {
        List<TrackEntity> trackEntities = new LinkedList<>();
        Date now = new Date();

        List<GenreEntity> genres = new LinkedList<>();

        String[] g_str = albumUploadDto.getGenre().split(", ");

        for(String g : g_str) {
            GenreEntity genre = genreRepository.findByName(g);
            if(Objects.isNull(genre)){
                genre = new GenreEntity();
                genre.setName(g);
                genre.setCreated(now);
                genre.setUpdated(now);
                genre = genreRepository.save(genre);
            }
            genres.add(genre);
        }

        Gson gson = new Gson();
        TrackUploadNewDto[] tracks = gson.fromJson(albumUploadDto.getTracks(), TrackUploadNewDto[].class);

        for(var track : tracks) {
            String title = track.getTitle();

            TrackEntity trackEntity = trackRepository.findByTitle(title);

            if(Objects.nonNull(trackEntity)) {
                System.out.println("Track already exist! Track - " + title);
                continue;
            }

            trackEntity = new TrackEntity();
            trackEntity.setTitle(title);

            trackEntity.setCreated(now);
            trackEntity.setUpdated(now);

            // create author for track
            {
                List<AuthorEntity> authors = new LinkedList<>();

                String[] a_str = track.getAuthor().split(", ");

                for(String a : a_str) {
                    AuthorEntity author = authorRepository.findByName(a);
                    if(Objects.isNull(author)){
                        author = new AuthorEntity();
                        author.setName(a);
                        author.setCreated(now);
                        author.setUpdated(now);
                        author = authorRepository.save(author);
                    }
                    authors.add(author);
                }

                trackEntity.setAuthors(authors);
            }

            // load genres for track
            {
                trackEntity.setGenres(genres);
            }

            trackEntities.add(trackEntity);
        }

        // create album

        PrimaryAlbumEntity album = primaryAlbumRepository.findByTitle(albumUploadDto.getTitle());

        if(Objects.isNull(album)) {
            album = new PrimaryAlbumEntity();
            album.setTitle(albumUploadDto.getTitle());
            album.setCreated(now);

            // create author for album
            {
                List<AuthorEntity> authors = album.getAuthors();

                if(Objects.isNull(authors)) {
                    authors = new LinkedList<>();
                    String[] a_str = albumUploadDto.getAuthor().split(", ");

                    for(String a : a_str) {
                        AuthorEntity author = authorRepository.findByName(a);
                        if(Objects.isNull(author)){
                            author = new AuthorEntity();
                            author.setName(a);
                            author.setCreated(now);
                            author.setUpdated(now);
                            author = authorRepository.save(author);
                        }
                        authors.add(author);
                    }

                    album.setAuthors(authors);
                }
            }
        }

        album.setUpdated(now);

        album = primaryAlbumRepository.save(album);

        // create image
        {
            byte[] f = gson.fromJson(albumUploadDto.getImage(), byte[].class);
            MultipartFile image = new MockMultipartFile(album.getTitle(),
                    album.getTitle() + "-" + album.getId() + ".jpg", "image/jpeg", f);
            album.setImage(imageService.saveImage(image, album.getTitle() + "-" + album.getId()));
        }

        // save all tracks
        for(int i = 0; i < trackEntities.size(); i ++) {
            trackEntities.get(i).setAlbum(album);
            trackEntities.set(i, trackRepository.save(trackEntities.get(i)));

            TrackEntity t = trackEntities.get(i);
            MultipartFile trackFile = new MockMultipartFile(t.getTitle(),
                    t.getTitle() + ".mp3", "audio/mpeg", tracks[i].getTrack());
            fileService.saveTrack(t.getId(), trackFile);
        }

        // Add track to album
        {
            if(trackEntities.size() == 1)
                album.setAlbumTypeEntity(albumTypeRepository.findById((long) (AlbumTypeEnum.SINGLE.ordinal() + 1)).orElse(null));
            else
                album.setAlbumTypeEntity(albumTypeRepository.findById((long) (AlbumTypeEnum.ALBUM.ordinal() + 1)).orElse(null));

            album.setTracks(trackEntities);

            primaryAlbumRepository.save(album);
        }
    }
}
