package com.dam1rka.musicserver.services;

import com.dam1rka.musicserver.dtos.AlbumDto;
import com.dam1rka.musicserver.dtos.AlbumUploadDto;
import com.dam1rka.musicserver.dtos.TrackDto;
import com.dam1rka.musicserver.dtos.TrackUploadNewDto;
import com.dam1rka.musicserver.entities.*;
import com.dam1rka.musicserver.repositories.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final PrimaryAlbumRepository primaryAlbumRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final AlbumTypeRepository albumTypeRepository;
    private final ImageService imageService;
    private final FileService fileService;
    private final FileUploaderService fileUploaderService;
    private final LikeService likeService;

    @Value("${file-server}")
    private String fileServer;

    @Autowired
    public AlbumService(PrimaryAlbumRepository primaryAlbumRepository, AlbumRepository albumRepository, TrackRepository trackRepository,
                        AuthorRepository authorRepository, GenreRepository genreRepository, ImageService imageService, FileService fileService,
                        AlbumTypeRepository albumTypeRepository, FileUploaderService fileUploaderService, LikeService likeService) {
        this.primaryAlbumRepository = primaryAlbumRepository;
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.albumTypeRepository = albumTypeRepository;
        this.imageService = imageService;
        this.fileService = fileService;
        this.fileUploaderService = fileUploaderService;
        this.likeService = likeService;
    }

    public AlbumDto getAlbum(UserEntity user, Long id) throws RuntimeException {
        AlbumEntity album = albumRepository.findById(id).orElse(null);

        if(Objects.isNull(album))
            throw new RuntimeException("Album not found");

        AlbumDto albumDto = AlbumDto.fromAlbumEntity(album, fileServer);

        albumDto.setImageUrl(fileServer + "images/" + album.getImage().getId());

        if(Objects.nonNull(user))
            albumDto.setLiked(likeService.containAlbum(user, album));

        return albumDto;
    }

    public PrimaryAlbumEntity getPrimaryAlbum(Long id) throws RuntimeException {
        PrimaryAlbumEntity album = primaryAlbumRepository.findById(id).orElse(null);
        if(Objects.isNull(album))
            throw new RuntimeException("Album not found");

        return album;
    }

    public List<TrackDto> getTracks(UserEntity user, long id) throws RuntimeException {
        AlbumEntity album = albumRepository.findById(id).orElse(null);

        if(Objects.isNull(album))
            return null;

        if(id == 1) // Плейлист - Всё в одном
            album.getTracks().sort(Comparator.comparing(TrackEntity::getId).reversed());

        List<TrackDto> tracks = new LinkedList<>();

        List<TrackEntity> likedTracks;

        if(Objects.nonNull(user))
            likedTracks = likeService.getLikedByType(user, AlbumTypeEnum.PLAYLIST).get(0).getTracks();
        else
            likedTracks = new LinkedList<>();


        for(TrackEntity track : album.getTracks()) {
            TrackDto t = new TrackDto();

            t.setId(track.getId());
            t.setTitle(track.getTitle());
            t.setUrl(fileServer + "tracks/" + t.getId());

            t.setLiked(likedTracks.contains(track));

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

            AlbumEntity albumEntity = track.getAlbum();
            if(Objects.nonNull(albumEntity)) {
                t.setAlbumId(albumEntity.getId());
                t.setAlbum(albumEntity.getTitle());

                long imageId = albumEntity.getImage().getId();
                t.setImageUrl(fileServer + "mediumImages/" + imageId);
                t.setMetadataImageUrl(fileServer + "images/" + imageId);
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

    public void uploadTest(AlbumUploadDto albumUploadDto) {

        AlbumEntity album = new AlbumEntity();
        album.setTitle(albumUploadDto.getTitle());

        Gson gson = new Gson();

        // create image
        {
            byte[] f = gson.fromJson(albumUploadDto.getImage(), byte[].class);
            MultipartFile bigImageFile = new MockMultipartFile(album.getTitle(),
                    album.getTitle() + "-" + album.getId() + ".jpg", "image/jpeg", f);

            // Send big image
            long id = fileUploaderService.upload(bigImageFile, FileUploaderService.FileType.Image);
        }
    }

    public void uploadAlbum(AlbumUploadDto albumUploadDto) {
//        uploadTest(albumUploadDto);

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

        // create album

        AlbumEntity album;

        {
            // create author for album
            List<AuthorEntity> authors = new LinkedList<>();

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

            album = albumRepository.findByTitleAndAuthorsIn(albumUploadDto.getTitle(), authors);

            if(Objects.isNull(album)) {
                album = new AlbumEntity();
                album.setTitle(albumUploadDto.getTitle());
                album.setCreated(now);
                album.setAuthors(authors);
                album.setDescription(authors.stream().map(AuthorEntity::getName).collect(Collectors.joining(", ")));
            }

            album.setUpdated(now);

            album = albumRepository.save(album);
        }

        // create image
        {
            byte[] f = gson.fromJson(albumUploadDto.getImage(), byte[].class);
            MultipartFile bigImageFile = new MockMultipartFile(album.getTitle(),
                    album.getTitle() + "-" + album.getId() + ".jpg", "image/jpeg", f);

            // Send big image
            long id = fileUploaderService.upload(bigImageFile, FileUploaderService.FileType.Image);

            // save image
            album.setImage(imageService.saveImage(id, bigImageFile, album.getTitle() + "-" + album.getId()));

            // Send medium
            ImageEntity e = album.getImage();

            byte[] mediumImg = imageService.getMediumImage(album.getImage().getId());

            MockMultipartFile mediumImageFile = new MockMultipartFile(e.getUrl(),
                    e.getUrl() + ".jpg", "image/jpeg", mediumImg);

            fileUploaderService.upload(mediumImageFile, FileUploaderService.FileType.MediumImage);

            // Send small
            byte[] smallImg = imageService.getSmailImage(e.getId());

            MockMultipartFile smallImageFile = new MockMultipartFile(e.getUrl(),
                    e.getUrl() + ".jpg", "image/jpeg", smallImg);

            fileUploaderService.upload(smallImageFile, FileUploaderService.FileType.SmallImage);

        }

        TrackUploadNewDto[] tracks = gson.fromJson(albumUploadDto.getTracks(), TrackUploadNewDto[].class);

        for(var track : tracks) {
            String title = track.getTitle();

            // create author for track
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

            TrackEntity trackEntity = trackRepository.findByTitleAndAlbumAndAuthorsIn(title, album, authors);

            if(Objects.isNull(trackEntity)) {
                trackEntity = new TrackEntity();
                trackEntity.setTitle(title);
                trackEntity.setAuthors(authors);
                trackEntity.setUpdated(now);
                trackEntity.setGenres(genres);

                trackEntity.setCreated(now);

                trackEntities.add(trackEntity);
            }
            else {
                trackEntity.setUpdated(now);
                trackEntity.setGenres(genres);

                trackRepository.save(trackEntity);
            }
        }

        // save all tracks
        for(int i = 0; i < trackEntities.size(); i ++) {
            TrackEntity t = trackEntities.get(i);
            MultipartFile trackFile = new MockMultipartFile(t.getTitle(),
                    t.getTitle() + ".mp3", "audio/mpeg", tracks[i].getTrack());
            fileService.saveTrack(t.getId(), trackFile);

            long id = fileUploaderService.upload(trackFile, FileUploaderService.FileType.Track);

            if(id != -1)
                t.setId(id);

            t.setAlbum(album);

            trackEntities.set(i, trackRepository.save(t));
        }

        // Add track to album
        {
            if(trackEntities.size() == 1)
                album.setAlbumTypeEntity(albumTypeRepository.findById((long) (AlbumTypeEnum.SINGLE.ordinal() + 1)).orElse(null));
            else
                album.setAlbumTypeEntity(albumTypeRepository.findById((long) (AlbumTypeEnum.ALBUM.ordinal() + 1)).orElse(null));

            album.setTracks(trackEntities);

            albumRepository.save(album);
        }
    }
}
