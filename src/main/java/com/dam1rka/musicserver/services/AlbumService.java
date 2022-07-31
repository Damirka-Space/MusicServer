package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.TrackUploadDto;
import com.dam1rka.musicserver.dtos.TrackDto;
import com.dam1rka.musicserver.entities.AlbumEntity;
import com.dam1rka.musicserver.entities.AuthorEntity;
import com.dam1rka.musicserver.entities.PrimaryAlbumEntity;
import com.dam1rka.musicserver.entities.TrackEntity;
import com.dam1rka.musicserver.repositories.AlbumRepository;
import com.dam1rka.musicserver.repositories.PrimaryAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class AlbumService {

    @Autowired
    private PrimaryAlbumRepository primaryAlbumRepository;
    private AlbumRepository albumRepository;

    private FileService fileService;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, FileService fileService) {
        this.albumRepository = albumRepository;
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

        List<TrackDto> tracks = new ArrayList<>();

        for(TrackEntity track : album.getTracks()) {
            TrackDto t = new TrackDto();

            t.setId(track.getId());
            t.setTitle(track.getTitle());

            PrimaryAlbumEntity primaryAlbum = track.getAlbum();
            if(Objects.nonNull(primaryAlbum)) {
                t.setAlbumId(primaryAlbum.getId());
                t.setAlbum(primaryAlbum.getTitle());

                List<AuthorEntity> authors = primaryAlbum.getAuthors();

                if(Objects.nonNull(authors)) {
                    List<Long> ids = new ArrayList<>();
                    List<String> ath = new ArrayList<>();
                    for(AuthorEntity author : authors) {
                        ids.add(author.getId());
                        ath.add(author.getName());
                    }

                    t.setAuthorId(ids);
                    t.setAuthor(ath);
                }
            }
            tracks.add(t);
        }
        return tracks;
    }

    public byte[] loadImage(Long id) {
        PrimaryAlbumEntity album = getPrimaryAlbum(id);
        if(Objects.isNull(album))
            throw new RuntimeException("Album not found");

        if(Objects.isNull(album.getImage()))
            throw new RuntimeException("No image in album");

        return fileService.loadImage(album.getImage().getUrl());
    }

}
