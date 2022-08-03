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

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class AlbumService {

    private PrimaryAlbumRepository primaryAlbumRepository;
    private AlbumRepository albumRepository;

    private ImageService imageService;

    @Autowired
    public AlbumService(PrimaryAlbumRepository primaryAlbumRepository,AlbumRepository albumRepository, ImageService imageService) {
        this.primaryAlbumRepository = primaryAlbumRepository;
        this.albumRepository = albumRepository;
        this.imageService = imageService;
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

}
