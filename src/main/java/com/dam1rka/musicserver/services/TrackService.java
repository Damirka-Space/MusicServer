package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.TrackUploadDto;
import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrackService {

    @Value("${music-format}")
    private String format;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AlbumTypeRepository albumTypeRepository;
    @Autowired
    private PrimaryAlbumRepository primaryAlbumRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private TrackRepository trackRepository;

    private FileService fileService;

    @Autowired
    TrackService(TrackRepository trackRepository, FileService fileService) {
        this.trackRepository = trackRepository;
        this.fileService = fileService;
    }

    public void saveTrack(TrackUploadDto trackUploadDto) {
        if(!Objects.requireNonNull(trackUploadDto.getTrack().getOriginalFilename()).contains(format))
            throw new RuntimeException("Invalid file");

        String title = trackUploadDto.getTitle();

        TrackEntity trackEntity = trackRepository.findByTitle(title);

        if(Objects.nonNull(trackEntity))
            throw new RuntimeException("Track already exist!");

        trackEntity = new TrackEntity();
        trackEntity.setTitle(title);

        Date now = new Date();
        trackEntity.setCreated(now);
        trackEntity.setUpdated(now);

        // create album

        PrimaryAlbumEntity album = primaryAlbumRepository.findByTitle(trackUploadDto.getAlbum());

        if(Objects.isNull(album)) {
            album = new PrimaryAlbumEntity();
            album.setTitle(trackUploadDto.getAlbum());
            album.setCreated(now);

            // create image
            {
                String url = Objects.requireNonNull(trackUploadDto.getImage().getOriginalFilename());
                ImageEnitiy imageEnitiy = imageRepository.findByUrl(url);

                if(Objects.isNull(imageEnitiy)) {
                    imageEnitiy = new ImageEnitiy();
                    imageEnitiy.setUrl(url);
                    imageEnitiy.setCreated(now);
                    imageEnitiy.setUpdated(now);
                    imageEnitiy = imageRepository.save((imageEnitiy));
                    fileService.saveImage(trackUploadDto.getImage());
                }

                album.setImage(imageEnitiy);
            }

            // create author
            {
                AuthorEntity author = authorRepository.findByName(trackUploadDto.getAuthor());
                if(Objects.isNull(author)){
                    author = new AuthorEntity();
                    author.setName(trackUploadDto.getAuthor());
                    author.setCreated(now);
                    author.setUpdated(now);
                    author = authorRepository.save(author);
                }

                album.setAuthors(Collections.singletonList(author));
            }

            album.setAlbumTypeEntity(albumTypeRepository.findById((long) (AlbumTypeEnum.ALBUM.ordinal() + 1)).orElse(null));
        }

        album.setUpdated(now);

        album = primaryAlbumRepository.save(album);

        trackEntity.setAlbum(album);
        trackEntity = trackRepository.save(trackEntity);

        // Add track to album
        {
            List<TrackEntity> tracks = album.getTracks();
            if(Objects.isNull(tracks))
                tracks = new LinkedList<>();
            tracks.add(trackEntity);
            album.setTracks(tracks);

            primaryAlbumRepository.save(album);
        }

        fileService.saveTrack(trackEntity.getId(), trackUploadDto.getTrack());
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
